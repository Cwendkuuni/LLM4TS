package nu.staldal.lagoon;

import java.awt.event.*;
import org.xml.sax.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class LagoonGUI extends Frame implements WindowListener
{
    private static final boolean DEBUG = false;
    private Panel buttonPanel;
    private Panel inputPanel;
    private Panel centerPanel;
    private Button loadButton;
    private Button saveButton;
    private Button buildButton;
    private Button forceButton;
    private Button exitButton;
    private InputComponent sitemapFile;
    private InputComponent sourceDir;
    private InputComponent targetURL;
    private String password;
    private Label statusLabel;
    private TextArea progressArea;
    private LagoonProcessor processor;
    private long sitemapLastModified;
    
    public static void main(final String[] args) throws Exception {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        if (args.length > 0) {
            appFrame.loadProperties(new File(args[0]));
        }
    }
    
    LagoonGUI() {
        super("Lagoon");
        this.processor = null;
        this.sitemapLastModified = 0L;
        this.addWindowListener(this);
        this.add(this.inputPanel = new Panel(), "North");
        this.inputPanel.setLayout(new GridLayout(0, 1));
        this.inputPanel.add(this.sitemapFile = new InputComponent(this, "Sitemap file", "", true));
        this.inputPanel.add(this.sourceDir = new InputComponent(this, "Source directory", "."));
        this.inputPanel.add(this.targetURL = new InputComponent(this, "Target URL", "."));
        this.add(this.centerPanel = new Panel(), "Center");
        this.centerPanel.setLayout(new BorderLayout());
        this.centerPanel.add(this.progressArea = new TextArea(null, 5, 50, 1), "North");
        this.progressArea.setEditable(false);
        this.centerPanel.add(this.statusLabel = new Label("Not initialized"), "South");
        this.add(this.buttonPanel = new Panel(), "South");
        this.buttonPanel.add(this.loadButton = new Button("Load Properties"));
        this.loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                LagoonGUI.this.loadPropertyFile();
            }
        });
        this.buttonPanel.add(this.saveButton = new Button("Save Properties"));
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                LagoonGUI.this.savePropertyFile();
            }
        });
        this.buttonPanel.add(this.buildButton = new Button("Build"));
        this.buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                LagoonGUI.this.build(false);
            }
        });
        this.buttonPanel.add(this.forceButton = new Button("Force build"));
        this.forceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                LagoonGUI.this.build(true);
            }
        });
        this.buttonPanel.add(this.exitButton = new Button("Exit"));
        this.exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                LagoonGUI.this.exit();
            }
        });
        this.setBackground(Color.lightGray);
        this.pack();
    }
    
    @Override
    public void windowOpened(final WindowEvent e) {
    }
    
    @Override
    public void windowClosing(final WindowEvent e) {
        this.exit();
    }
    
    @Override
    public void windowClosed(final WindowEvent e) {
    }
    
    @Override
    public void windowIconified(final WindowEvent e) {
    }
    
    @Override
    public void windowDeiconified(final WindowEvent e) {
    }
    
    @Override
    public void windowActivated(final WindowEvent e) {
    }
    
    @Override
    public void windowDeactivated(final WindowEvent e) {
    }
    
    private void build(final boolean force) {
        this.loadButton.setEnabled(false);
        this.saveButton.setEnabled(false);
        this.buildButton.setEnabled(false);
        this.forceButton.setEnabled(false);
        this.sitemapFile.setEnabled(false);
        this.sourceDir.setEnabled(false);
        this.targetURL.setEnabled(false);
        final File sm = new File(this.sitemapFile.getValue());
        if (this.processor == null || this.sitemapFile.hasChanged() || this.sourceDir.hasChanged() || this.targetURL.hasChanged()) {
            this.init();
        }
        else if (sm.lastModified() > this.sitemapLastModified) {
            this.init();
        }
        if (this.processor != null) {
            final long timeBefore = System.currentTimeMillis();
            this.progressArea.setText("--- Start build ---\n");
            this.statusLabel.setText("Building...");
            try {
                final boolean success = this.processor.build(force);
                final long timeElapsed = System.currentTimeMillis() - timeBefore;
                if (!success) {
                    this.progressArea.append("--- Build finished with error(s) " + showTime(timeElapsed) + " ---");
                    final MessageDialog ed = new MessageDialog(this, "Building error", "Error(s) occured while building");
                    ed.show();
                }
                else {
                    this.progressArea.append("--- Build finished successfully " + showTime(timeElapsed) + " ---");
                }
            }
            catch (FileNotFoundException e) {
                final MessageDialog ed2 = new MessageDialog(this, "Fatal Building error", "File not found: " + e.getMessage());
                ed2.show();
            }
            catch (IOException e2) {
                final MessageDialog ed2 = new MessageDialog(this, "Fatal Building error", e2.toString());
                ed2.show();
            }
            this.statusLabel.setText("Ready");
        }
        else {
            this.statusLabel.setText("Not initialized");
        }
        this.sitemapLastModified = sm.lastModified();
        this.loadButton.setEnabled(true);
        this.saveButton.setEnabled(true);
        this.buildButton.setEnabled(true);
        this.forceButton.setEnabled(true);
        this.sitemapFile.setEnabled(true);
        this.sourceDir.setEnabled(true);
        this.targetURL.setEnabled(true);
    }
    
    private void exit() {
        try {
            if (this.processor != null) {
                this.processor.destroy();
            }
        }
        catch (IOException e) {
            System.err.println("Error destroying LagoonProcessor: " + e.toString());
        }
        System.exit(0);
    }
    
    private void init() {
        this.statusLabel.setText("Initializing...");
        try {
            if (this.processor != null) {
                this.processor.destroy();
            }
        }
        catch (IOException e) {
            System.err.println("Error destroying LagoonProcessor: " + e.toString());
        }
        this.processor = null;
        try {
            Element sitemapTree;
            try {
                final FileInputStream fis = new FileInputStream(new File(this.sitemapFile.getValue()));
                final InputSource is = new InputSource(this.sitemapFile.getValue());
                is.setByteStream(fis);
                sitemapTree = TreeBuilder.parseXML(is, false);
                fis.close();
            }
            catch (SAXException e2) {
                final Exception ee = e2.getException();
                if (ee == null) {
                    e2.printStackTrace();
                    throw new LagoonException(e2.getMessage());
                }
                if (ee instanceof IOException) {
                    throw (IOException)ee;
                }
                ee.printStackTrace();
                throw new LagoonException(ee.getMessage());
            }
            final PrintWriter pw = new PrintWriter(new TextAreaWriter(this.progressArea), true);
            this.processor = new LagoonProcessor(this.targetURL.getValue(), sitemapTree, new File(this.sourceDir.getValue()), this.password, pw, pw);
        }
        catch (AuthenticationMissingException e5) {
            final PasswordDialog pwd = new PasswordDialog(this, "Password", "Password needed to access target");
            pwd.show();
            this.password = pwd.getPassword();
            if (this.password != null) {
                this.init();
            }
        }
        catch (AuthenticationException e6) {
            final PasswordDialog pwd = new PasswordDialog(this, "Password", "Invalid password, try again");
            pwd.show();
            this.password = pwd.getPassword();
            if (this.password != null) {
                this.init();
            }
        }
        catch (FileNotFoundException e3) {
            final MessageDialog ed = new MessageDialog(this, "Initializing error", "File not found: " + e3.getMessage());
            ed.show();
        }
        catch (IOException e) {
            final MessageDialog ed = new MessageDialog(this, "Initializing error", e.toString());
            ed.show();
        }
        catch (LagoonException e4) {
            final MessageDialog ed = new MessageDialog(this, "Initializing error", e4.getMessage());
            ed.show();
        }
    }
    
    private void loadPropertyFile() {
        final FileDialog propertyFileDialog = new FileDialog(this, "Load property file");
        propertyFileDialog.show();
        final File propertyFile = (propertyFileDialog.getFile() == null) ? null : new File(propertyFileDialog.getDirectory(), propertyFileDialog.getFile());
        if (propertyFile != null) {
            this.loadProperties(propertyFile);
        }
    }
    
    private void loadProperties(final File propertyFile) {
        try {
            final FileInputStream fis = new FileInputStream(propertyFile);
            final Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            this.sitemapFile.setValue(getProperty(prop, "sitemapFile"));
            this.sourceDir.setValue(getProperty(prop, "sourceDir"));
            this.targetURL.setValue(getProperty(prop, "targetURL"));
            this.password = prop.getProperty("password");
        }
        catch (LagoonException e) {
            final MessageDialog ed = new MessageDialog(this, "Error in propertry file", e.getMessage());
            ed.show();
        }
        catch (IOException e2) {
            final MessageDialog ed = new MessageDialog(this, "Error reading property file", e2.toString());
            ed.show();
        }
    }
    
    private void savePropertyFile() {
        final FileDialog propertyFileDialog = new FileDialog(this, "Save property file", 1);
        propertyFileDialog.show();
        final File propertyFile = (propertyFileDialog.getFile() == null) ? null : new File(propertyFileDialog.getDirectory(), propertyFileDialog.getFile());
        if (propertyFile != null) {
            try {
                final FileOutputStream fos = new FileOutputStream(propertyFile);
                final Properties prop = new Properties();
                prop.setProperty("sitemapFile", this.sitemapFile.getValue());
                prop.setProperty("sourceDir", this.sourceDir.getValue());
                prop.setProperty("targetURL", this.targetURL.getValue());
                if (this.password != null) {
                    final YesNoQueryDialog ynDialog = new YesNoQueryDialog(this, "Save property file", "Save password in property file?", "Yes", "No");
                    ynDialog.show();
                    if (ynDialog.getResult()) {
                        prop.setProperty("password", this.password);
                    }
                }
                prop.store(fos, "Lagoon properties");
                fos.close();
                this.statusLabel.setText("Properties saved");
            }
            catch (IOException e) {
                final MessageDialog ed = new MessageDialog(this, "Error writing property file", e.toString());
                ed.show();
            }
        }
    }
    
    private static String getProperty(final Properties prop, final String name) throws LagoonException {
        final String value = prop.getProperty(name);
        if (value == null) {
            throw new LagoonException("Property " + name + " not specified");
        }
        return value.trim();
    }
    
    private static String showTime(final long ms) {
        if (ms < 10000L) {
            return "in " + ms + " ms";
        }
        return "in " + ms / 1000L + " s";
    }
}
