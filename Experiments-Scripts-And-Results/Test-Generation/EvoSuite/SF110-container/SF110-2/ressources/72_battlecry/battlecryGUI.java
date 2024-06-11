package bcry;

import java.util.*;
import java.awt.image.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class battlecryGUI extends JFrame implements ActionListener
{
    private final String TEMP_LAYOUT = "data/temp/temp_lo.dat";
    private final String BANNER = "data/images/bcBanner.jpg";
    private final String BACKGROUND_IMAGE = "data/images/bcBackground.jpg";
    private final String README = "readme.html";
    private final String DEFAULT_OUTPUT_FILE = "data/temp/output.txt";
    private int fontSize;
    private String fontColor;
    private boolean showBackground;
    private List currentLyrics;
    private bcFileDialog fd;
    private String currentModule;
    private JFrame owner;
    private JButton buttonExit;
    private JButton buttonGen;
    private JButton buttonClear;
    private JButton buttonModinfo;
    private JButton buttonLoad;
    private JButton buttonClearLayout;
    private JMenuBar mainMenu;
    private JMenu menuFile;
    private JMenu menuView;
    private JMenu menuTools;
    private JMenu menuHelp;
    private JMenu submenuFont;
    private JMenu submenuFontSize;
    private JMenu submenuFontColor;
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuOpenLayout;
    private JMenuItem menuSaveLayout;
    private JMenuItem menuExit;
    private JMenuItem menuAbout;
    private JMenuItem menuEditor;
    private JMenuItem menuConverter;
    private JMenuItem menuReadme;
    private JMenuItem menuToggleImage;
    private JMenuItem submenuFontSize2;
    private JMenuItem submenuFontSize3;
    private JMenuItem submenuFontSize4;
    private JMenuItem submenuFontSize5;
    private JMenuItem submenuFontSize6;
    private JMenuItem submenuFontColorBlack;
    private JMenuItem submenuFontColorGrey;
    private JPanel panel;
    private JPanel subpanel1;
    private JPanel subpanel2;
    private JPanel textPanel;
    private JPanel sideBarLeft;
    private JPanel sideBarRight;
    private JPanel logPanel;
    private JPanel layoutPanel;
    private TextArea logWindow;
    private JTextArea layoutField;
    private JEditorPane textField;
    private JScrollPane mainScroll;
    private bcGenerator bc;
    private bcVoice voice;
    private bcModule module;
    private static battlecryGUI f;
    
    public battlecryGUI() {
        super("Battlecry 0.2cvs");
        this.fontSize = 4;
        this.fontColor = "#c0c0c0";
        this.showBackground = true;
        this.currentModule = "";
        this.mainMenu = new JMenuBar();
        this.panel = new JPanel();
        this.subpanel1 = new JPanel();
        this.subpanel2 = new JPanel();
        this.textPanel = new JPanel();
        this.sideBarLeft = new JPanel();
        this.sideBarRight = new JPanel();
        this.logPanel = new JPanel();
        this.layoutPanel = new JPanel();
        this.logWindow = new TextArea(5, 80);
        this.layoutField = new JTextArea(3, 12);
        this.bc = null;
        this.voice = null;
        this.module = null;
        this.initComponents();
    }
    
    private void initComponents() {
        (this.menuFile = new JMenu("File")).setMnemonic('F');
        (this.menuOpen = new JMenuItem("Open Module...", 111)).addActionListener(this);
        (this.menuSave = new JMenuItem("Save Lyrics...", 115)).addActionListener(this);
        (this.menuOpenLayout = new JMenuItem("Open Layout...", 108)).addActionListener(this);
        (this.menuSaveLayout = new JMenuItem("Save Layout...", 121)).addActionListener(this);
        (this.menuExit = new JMenuItem("Exit", 120)).addActionListener(this);
        this.menuFile.add(this.menuOpen);
        this.menuFile.add(this.menuSave);
        this.menuFile.addSeparator();
        this.menuFile.add(this.menuOpenLayout);
        this.menuFile.add(this.menuSaveLayout);
        this.menuFile.addSeparator();
        this.menuFile.add(this.menuExit);
        (this.menuView = new JMenu("View")).setMnemonic('V');
        (this.menuToggleImage = new JMenuItem("Toggle Background", 98)).addActionListener(this);
        (this.submenuFont = new JMenu("Font")).setMnemonic('N');
        this.submenuFontSize = new JMenu("Size");
        (this.submenuFontSize2 = new JMenuItem("Very Small")).addActionListener(this);
        (this.submenuFontSize3 = new JMenuItem("Small")).addActionListener(this);
        (this.submenuFontSize4 = new JMenuItem("Normal")).addActionListener(this);
        (this.submenuFontSize5 = new JMenuItem("Large")).addActionListener(this);
        (this.submenuFontSize6 = new JMenuItem("Very Large")).addActionListener(this);
        this.submenuFontSize.add(this.submenuFontSize2);
        this.submenuFontSize.add(this.submenuFontSize3);
        this.submenuFontSize.add(this.submenuFontSize4);
        this.submenuFontSize.add(this.submenuFontSize5);
        this.submenuFontSize.add(this.submenuFontSize6);
        this.submenuFontColor = new JMenu("Color");
        (this.submenuFontColorBlack = new JMenuItem("Black")).addActionListener(this);
        (this.submenuFontColorGrey = new JMenuItem("Grey")).addActionListener(this);
        this.submenuFontColor.add(this.submenuFontColorBlack);
        this.submenuFontColor.add(this.submenuFontColorGrey);
        this.submenuFont.add(this.submenuFontColor);
        this.submenuFont.add(this.submenuFontSize);
        this.menuView.add(this.menuToggleImage);
        this.menuView.add(this.submenuFont);
        (this.menuHelp = new JMenu("Help")).setMnemonic('h');
        (this.menuReadme = new JMenuItem("Show Readme", 114)).addActionListener(this);
        (this.menuAbout = new JMenuItem("About...", 97)).addActionListener(this);
        this.menuHelp.add(this.menuReadme);
        this.menuHelp.add(this.menuAbout);
        (this.menuTools = new JMenu("Tools")).setMnemonic('t');
        (this.menuEditor = new JMenuItem("Module Editor", 101)).addActionListener(this);
        (this.menuConverter = new JMenuItem("List Converter", 99)).addActionListener(this);
        this.menuTools.add(this.menuEditor);
        this.menuTools.add(this.menuConverter);
        this.mainMenu.add(this.menuFile);
        this.mainMenu.add(this.menuView);
        this.mainMenu.add(this.menuHelp);
        this.setJMenuBar(this.mainMenu);
        (this.textField = new JEditorPane() {
            public void paintComponent(final Graphics g) {
                final ImageIcon icon = new ImageIcon("data/images/bcBackground.jpg");
                final Dimension d = this.getSize();
                g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
                this.setOpaque(false);
                super.paintComponent(g);
            }
        }).setBackground(new Color(100, 100, 210));
        this.textField.setContentType("text/html");
        (this.mainScroll = new JScrollPane(this.textField)).setVerticalScrollBarPolicy(22);
        this.mainScroll.getViewport().setScrollMode(2);
        this.logWindow.setFont(new Font("SansSerif", 0, 10));
        this.logWindow.setEditable(false);
        this.layoutField.setFont(new Font("SansSerif", 0, 10));
        this.layoutField.setBackground(new Color(155, 155, 200));
        final JScrollPane layoutScroll = new JScrollPane(this.layoutField);
        (this.buttonExit = new JButton("Exit")).addActionListener(this);
        (this.buttonGen = new JButton("Generate")).addActionListener(this);
        (this.buttonClear = new JButton("Clear")).addActionListener(this);
        (this.buttonModinfo = new JButton("Module Info")).addActionListener(this);
        (this.buttonLoad = new JButton("Load Module")).addActionListener(this);
        (this.buttonClearLayout = new JButton("Clear")).addActionListener(this);
        this.logPanel.setLayout(new GridLayout(1, 0));
        this.logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log Window"));
        this.logPanel.add(this.logWindow);
        this.textPanel.setLayout(new BorderLayout());
        this.textPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lyrics"));
        this.textPanel.add("Center", this.mainScroll);
        this.textPanel.setMinimumSize(new Dimension(10, 40));
        this.layoutPanel.setLayout(new BorderLayout());
        this.layoutPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Custom Layout"));
        this.layoutPanel.add("Center", layoutScroll);
        this.layoutPanel.add("South", this.buttonClearLayout);
        this.sideBarRight.setLayout(new GridLayout(3, 1));
        this.sideBarRight.add(this.layoutPanel);
        this.sideBarRight.setMinimumSize(new Dimension(20, 10));
        this.sideBarLeft.setLayout(new GridLayout(5, 1));
        this.sideBarLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Actions"));
        this.sideBarLeft.add(this.buttonLoad);
        this.sideBarLeft.add(this.buttonGen);
        this.sideBarLeft.add(this.buttonClear);
        this.sideBarLeft.add(this.buttonModinfo);
        this.sideBarLeft.add(this.buttonExit);
        this.sideBarLeft.setMinimumSize(new Dimension(20, 10));
        this.subpanel2.setLayout(new BorderLayout());
        this.subpanel2.add("East", this.sideBarRight);
        this.subpanel2.add("Center", this.textPanel);
        this.subpanel1.setLayout(new BorderLayout());
        this.subpanel1.setOpaque(false);
        this.subpanel1.add("West", this.sideBarLeft);
        this.subpanel1.add("Center", this.subpanel2);
        final JPanel subpanel3 = new JPanel();
        subpanel3.setLayout(new BorderLayout());
        subpanel3.add("Center", this.subpanel1);
        subpanel3.add("North", new JLabel(new ImageIcon("data/images/bcBanner.jpg")));
        this.panel.setLayout(new BorderLayout());
        this.panel.add("Center", subpanel3);
        this.panel.add("South", this.logPanel);
        this.getContentPane().add(this.panel);
        this.pack();
        this.setDefaultCloseOperation(0);
        this.setSize(800, 600);
        this.setResizable(true);
        final Window wnd = new Window(this);
        this.setLocation((int)(wnd.getToolkit().getScreenSize().getWidth() / 2.0 - this.getWidth() / 2), (int)(wnd.getToolkit().getScreenSize().getHeight() / 2.0 - this.getHeight() / 2));
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                System.exit(0);
            }
        });
        this.guiLog("Initialized.");
        final boolean[] options = { true, false };
        this.voice = new bcVoice(options, this);
    }
    
    private void lyricsToTextField() {
        if (this.bc != null) {
            this.drawCurrentLyrics(this.currentLyrics = this.bc.getLyrics());
        }
        else {
            this.guiLog("You must load a module first.");
        }
    }
    
    private void writeLayoutToFile(final String filename) {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(this.layoutField.getText());
            bw.close();
        }
        catch (IOException ex) {}
    }
    
    private void writeLyricsToFile(final String filename) {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            if (!this.textField.getText().equals("")) {
                bw.write(this.textField.getText());
                bw.newLine();
                bw.newLine();
                bw.close();
            }
            else {
                this.guiLog("Please load a module and generate a set of lyrics first.");
            }
        }
        catch (IOException ex) {}
    }
    
    private void loadModule() {
        String layout = "";
        this.fd = new bcFileDialog("bcm", "Battlecry Module");
        if (!this.layoutField.getText().equals("")) {
            final String temp = this.layoutField.getText().substring(this.layoutField.getText().length() - 3);
            if (temp.equals("dat")) {
                layout = this.layoutField.getText();
                this.guiLog("Trying to open layout file " + this.layoutField.getText());
            }
            else {
                this.guiLog("Saving custom layout...");
                this.writeLayoutToFile("data/temp/temp_lo.dat");
                layout = "data/temp/temp_lo.dat";
            }
        }
        if (this.currentModule.equals("")) {
            this.clearLayout();
            this.currentModule = this.fd.openDialog().getPath();
            this.buttonLoad.setText("Reload");
        }
        if (!this.currentModule.equals("")) {
            if (this.module != null && this.module.getDictionary() != null) {
                this.module = new bcModule(this.currentModule, layout, this.voice, this.module.getDictionary());
            }
            else {
                this.module = new bcModule(this.currentModule, layout, this.voice);
            }
            this.bc = new bcGenerator(this.module, this.voice);
        }
    }
    
    private void readModuleInfo() {
        if (this.bc != null) {
            this.bc.getModuleInfo();
        }
        else {
            this.guiLog("You must load a module first.");
        }
    }
    
    private void clearLayout() {
        if (!this.layoutField.getText().equals("")) {
            this.layoutField.setText("");
            if (!this.currentModule.equals("")) {
                this.guiLog("Reloading Module...");
                this.loadModule();
            }
        }
    }
    
    private void showReadme() {
        try {
            this.textField.setPage("file://localhost/" + new File("readme.html").getAbsoluteFile());
        }
        catch (IOException e) {
            this.guiLog(e.toString());
        }
    }
    
    private void clearMainWindow() {
        this.textField.setText("");
    }
    
    private void toggleBackground() {
        if (this.showBackground) {
            this.showBackground = false;
            this.textField = new JEditorPane();
            this.fontColor = "#000000";
        }
        else {
            this.showBackground = true;
            this.textField = new JEditorPane() {
                public void paintComponent(final Graphics g) {
                    final ImageIcon icon = new ImageIcon("data/images/bcBackground.jpg");
                    final Dimension d = this.getSize();
                    g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
                    this.setOpaque(false);
                    super.paintComponent(g);
                }
            };
            this.fontColor = "#c0c0c0";
        }
        this.textField.setBackground(new Color(100, 100, 210));
        this.textField.setContentType("text/html");
        this.mainScroll.setViewportView(this.textField);
        this.drawCurrentLyrics(this.currentLyrics);
    }
    
    private void drawCurrentLyrics(final List lyrics) {
        if (lyrics != null) {
            String output = "<html><font color=" + this.fontColor + "style=sans-serif size=" + (this.fontSize + 1) + "><b>";
            output = output + "<center>" + lyrics.get(1).substring(1, lyrics.get(1).length() - 1) + "</center></font><hr>";
            output = output + "<font color=" + this.fontColor + "style=sans-serif size=" + this.fontSize + ">";
            for (int i = 3; i < lyrics.size() - 1; ++i) {
                output = output + lyrics.get(i) + "<br>";
            }
            output += "<hr></b></font></html>";
            this.textField.setText(output);
        }
        else {
            this.guiLog("No lyrics found; you must load a module first.");
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.buttonExit || e.getSource() == this.menuExit) {
            System.out.println("Shutdown.");
            System.exit(0);
        }
        else if (e.getSource() == this.buttonGen) {
            this.lyricsToTextField();
        }
        else if (e.getSource() == this.buttonClear) {
            this.clearMainWindow();
        }
        else if (e.getSource() == this.buttonModinfo) {
            this.readModuleInfo();
        }
        else if (e.getSource() == this.buttonLoad) {
            this.loadModule();
        }
        else if (e.getSource() == this.menuOpen) {
            this.currentModule = "";
            this.loadModule();
        }
        else if (e.getSource() == this.menuSave) {
            this.writeLyricsToFile("data/temp/output.txt");
        }
        else if (e.getSource() == this.buttonClearLayout) {
            this.clearLayout();
        }
        else if (e.getSource() == this.menuReadme) {
            this.showReadme();
        }
        else if (e.getSource() == this.menuToggleImage) {
            this.toggleBackground();
        }
        else if (e.getSource() == this.submenuFontColorBlack) {
            this.fontColor = "#000000";
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontColorGrey) {
            this.fontColor = "#c0c0c0";
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontSize2) {
            this.fontSize = 2;
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontSize3) {
            this.fontSize = 3;
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontSize4) {
            this.fontSize = 4;
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontSize5) {
            this.fontSize = 5;
            this.drawCurrentLyrics(this.currentLyrics);
        }
        else if (e.getSource() == this.submenuFontSize6) {
            this.fontSize = 6;
            this.drawCurrentLyrics(this.currentLyrics);
        }
    }
    
    private void guiLog(final String text) {
        this.logWindow.append("Gui: " + text + '\n');
    }
    
    public void updateLog(final String text) {
        this.logWindow.append(text);
    }
    
    public static void main(final String[] args) {
        System.out.println("Battlecry GUI is starting...");
        battlecryGUI.f = new battlecryGUI();
    }
}
