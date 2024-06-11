import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class jgaapGUI extends JPanel implements ActionListener
{
    guiDriver driver;
    private JTextField currentAuthor;
    private ButtonGroup buttonGroup1;
    private ButtonGroup buttonGroup2;
    private JFileChooser chooseFile;
    private JButton buttonEvSet;
    private JPanel paneEvProgress;
    private JLabel labelev;
    private JProgressBar progressEventSet;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuFileExit;
    private JMenuItem menuFileDemo;
    private JMenu menuHelp;
    private JMenuItem menuHelpContents;
    private JMenuItem menuHelpAbout;
    private JTabbedPane paneOpts;
    private JPanel panelAcquire;
    private JPanel panelDocs;
    private JLabel labelKnown;
    private JScrollPane scrollPane1;
    private JTextArea listKnown;
    private JLabel labelUnknown;
    private JScrollPane scrollPane2;
    private JTextArea listUnknown;
    private JPanel panelOpts;
    private JRadioButton radioKnown;
    private JRadioButton radioUnknown;
    private JButton buttonLocal;
    private JPanel panelCanonicize;
    private JPanel panelCOpts;
    private JCheckBox[] canonicizers;
    private JRadioButton[] eventifiers;
    private ButtonGroup esButtonGroup;
    private JPanel panelProgress;
    private JButton buttonCanonicize;
    private JLabel label1;
    private JProgressBar progressCanonicize;
    private JLabel labelCanon;
    private JPanel panelAnalyze;
    private JLabel labelStatInf;
    private JPanel panelMethods;
    private JRadioButton radioCrossEnt;
    private JRadioButton radioLZW;
    private JPanel panelAnalProg;
    private JButton buttonAnalyze;
    private JLabel labelAnalProg;
    private JProgressBar progressAnalysis;
    private JScrollPane scrollPane3;
    private JTextArea listResults;
    private JPanel panelEventSet;
    private JPanel panelEOpts;
    private JPanel panelEvProgress;
    private JButton buttonEventSet;
    private JLabel labelevSet;
    
    public jgaapGUI() {
        this.driver = new guiDriver();
        this.initComponents();
    }
    
    private void initComponents() {
        this.currentAuthor = new JTextField();
        this.chooseFile = new JFileChooser();
        this.menuBar = new JMenuBar();
        this.menuFile = new JMenu();
        this.menuFileExit = new JMenuItem();
        this.menuFileDemo = new JMenuItem();
        this.menuHelp = new JMenu();
        this.menuHelpContents = new JMenuItem();
        this.menuHelpAbout = new JMenuItem();
        this.paneOpts = new JTabbedPane();
        this.panelAcquire = new JPanel();
        this.panelDocs = new JPanel();
        this.labelKnown = new JLabel();
        this.scrollPane1 = new JScrollPane();
        this.listKnown = new JTextArea(10, 30);
        this.labelUnknown = new JLabel();
        this.scrollPane2 = new JScrollPane();
        this.listUnknown = new JTextArea(10, 30);
        this.panelOpts = new JPanel();
        this.radioKnown = new JRadioButton();
        this.radioUnknown = new JRadioButton();
        this.buttonLocal = new JButton();
        this.panelCanonicize = new JPanel();
        this.panelCOpts = new JPanel();
        this.canonicizers = new JCheckBox[3];
        this.eventifiers = new JRadioButton[2];
        this.esButtonGroup = new ButtonGroup();
        this.panelProgress = new JPanel();
        this.buttonCanonicize = new JButton();
        this.label1 = new JLabel();
        this.progressCanonicize = new JProgressBar();
        this.labelCanon = new JLabel();
        this.panelAnalyze = new JPanel();
        this.labelStatInf = new JLabel();
        this.panelMethods = new JPanel();
        this.radioCrossEnt = new JRadioButton();
        this.radioLZW = new JRadioButton();
        this.panelAnalProg = new JPanel();
        this.buttonAnalyze = new JButton();
        this.labelAnalProg = new JLabel();
        this.progressAnalysis = new JProgressBar();
        this.scrollPane3 = new JScrollPane();
        this.listResults = new JTextArea(20, 30);
        this.buttonEventSet = new JButton();
        this.paneEvProgress = new JPanel();
        this.labelevSet = new JLabel();
        this.progressEventSet = new JProgressBar();
        this.panelEventSet = new JPanel();
        this.panelEOpts = new JPanel();
        this.panelEvProgress = new JPanel();
        this.setMinimumSize(new Dimension(400, 600));
        this.setPreferredSize(new Dimension(600, 400));
        this.setLayout(new BorderLayout());
        this.menuFile.setText("File");
        this.menuFileExit.setText("Exit");
        this.menuFile.add(this.menuFileExit);
        this.menuFileExit.addActionListener(this);
        this.menuFileDemo.setText("Load Demo");
        this.menuFileDemo.setActionCommand("demo");
        this.menuFileDemo.addActionListener(this);
        this.menuFile.add(this.menuFileDemo);
        this.menuBar.add(this.menuFile);
        this.menuHelp.setText("Help");
        this.menuHelpContents.setText("Contents (F1)");
        this.menuHelp.add(this.menuHelpContents);
        this.menuHelpAbout.setText("About");
        this.menuHelp.add(this.menuHelpAbout);
        this.menuBar.add(this.menuHelp);
        this.add(this.menuBar, "North");
        this.paneOpts.setFont(new Font("Garamond", 0, 14));
        this.panelAcquire.setLayout(new FlowLayout());
        this.panelDocs.setLayout(new BoxLayout(this.panelDocs, 1));
        this.labelKnown.setText("Registered Known Documents");
        this.labelKnown.setHorizontalAlignment(0);
        this.labelKnown.setHorizontalTextPosition(0);
        this.panelDocs.add(this.labelKnown);
        this.listKnown.setEnabled(false);
        this.scrollPane1.setViewportView(this.listKnown);
        this.panelDocs.add(this.scrollPane1);
        this.labelUnknown.setText("Registered Unknown Documents");
        this.labelUnknown.setHorizontalAlignment(0);
        this.labelUnknown.setHorizontalTextPosition(0);
        this.panelDocs.add(this.labelUnknown);
        this.listUnknown.setEnabled(false);
        this.scrollPane2.setViewportView(this.listUnknown);
        this.panelDocs.add(this.scrollPane2);
        this.panelAcquire.add(this.panelDocs);
        this.panelOpts.setLayout(new BoxLayout(this.panelOpts, 1));
        this.radioUnknown.setText("Unknown Author");
        this.radioUnknown.setActionCommand("unknownauthor");
        this.radioUnknown.addActionListener(this);
        this.panelOpts.add(this.radioUnknown);
        this.radioKnown.setText("Known Author");
        this.radioKnown.setActionCommand("knownauthor");
        this.radioKnown.addActionListener(this);
        this.radioKnown.setSelected(true);
        this.panelOpts.add(this.radioKnown);
        this.panelOpts.add(new JLabel("Author:"));
        this.panelOpts.add(this.currentAuthor);
        this.buttonLocal.setText("Local Document");
        this.panelOpts.add(this.buttonLocal);
        this.buttonLocal.setActionCommand("Import");
        this.buttonLocal.addActionListener(this);
        this.panelAcquire.add(this.panelOpts);
        this.paneOpts.addTab("Acquire", this.panelAcquire);
        this.panelCanonicize.setLayout(new BorderLayout());
        this.panelCOpts.setLayout(new GridLayout(12, 5));
        (this.canonicizers[0] = new JCheckBox()).setText("Smash Case");
        this.canonicizers[0].setToolTipText("Converts all text to lower case");
        this.panelCOpts.add(this.canonicizers[0]);
        (this.canonicizers[1] = new JCheckBox()).setText("Normalize Whitespace");
        this.canonicizers[1].setToolTipText("Converts all whitespace characters (newline, space and tab) to a single space");
        this.panelCOpts.add(this.canonicizers[1]);
        (this.canonicizers[2] = new JCheckBox()).setText("Strip HTML");
        this.canonicizers[2].setToolTipText("Removes all HTML tags and converts hexidecimal representations to actual character representations");
        this.panelCOpts.add(this.canonicizers[2]);
        this.panelCanonicize.add(this.panelCOpts, "Center");
        this.panelProgress.setLayout(new BoxLayout(this.panelProgress, 0));
        this.buttonCanonicize.setText("Canonicize");
        this.buttonCanonicize.addActionListener(this);
        this.panelProgress.add(this.buttonCanonicize);
        this.label1.setText("  Progress:  ");
        this.label1.setHorizontalAlignment(0);
        this.label1.setHorizontalTextPosition(0);
        this.panelProgress.add(this.label1);
        this.panelProgress.add(this.progressCanonicize);
        this.panelCanonicize.add(this.panelProgress, "South");
        this.labelCanon.setText("Document Canonicization");
        this.labelCanon.setHorizontalAlignment(0);
        this.panelCanonicize.add(this.labelCanon, "North");
        this.paneOpts.addTab("Canonicize", this.panelCanonicize);
        this.panelEventSet.setLayout(new BorderLayout());
        this.panelEOpts.setLayout(new GridLayout(12, 5));
        (this.eventifiers[0] = new JRadioButton()).setText("Individual Characters");
        this.eventifiers[0].setActionCommand("characters");
        this.esButtonGroup.add(this.eventifiers[0]);
        this.panelEOpts.add(this.eventifiers[0]);
        (this.eventifiers[1] = new JRadioButton()).setText("Entire Words");
        this.eventifiers[1].setActionCommand("words");
        this.esButtonGroup.add(this.eventifiers[1]);
        this.panelEOpts.add(this.eventifiers[1]);
        this.panelEventSet.add(this.panelEOpts, "Center");
        this.panelEvProgress.setLayout(new BoxLayout(this.panelEvProgress, 0));
        this.buttonEventSet.setText("Create Event Set");
        this.buttonEventSet.setActionCommand("CreateEventSet");
        this.buttonEventSet.addActionListener(this);
        this.panelEvProgress.add(this.buttonEventSet);
        this.labelevSet.setText("  Progress:  ");
        this.labelevSet.setHorizontalAlignment(0);
        this.labelevSet.setHorizontalTextPosition(0);
        this.panelEvProgress.add(this.labelevSet);
        this.panelEvProgress.add(this.progressEventSet);
        this.panelEventSet.add(this.panelEvProgress, "South");
        this.paneOpts.addTab("Event Set", this.panelEventSet);
        this.panelAnalyze.setLayout(new BorderLayout());
        this.labelStatInf.setText("Statistical Inference");
        this.labelStatInf.setHorizontalAlignment(0);
        this.panelAnalyze.add(this.labelStatInf, "North");
        this.panelMethods.setLayout(new BoxLayout(this.panelMethods, 1));
        this.radioCrossEnt.setText("Cross Entropy");
        this.radioCrossEnt.setActionCommand("crossentropy");
        this.panelMethods.add(this.radioCrossEnt);
        this.radioLZW.setText("LZW");
        this.radioLZW.setActionCommand("lzw");
        this.panelMethods.add(this.radioLZW);
        this.panelAnalyze.add(this.panelMethods, "West");
        this.panelAnalProg.setLayout(new BoxLayout(this.panelAnalProg, 0));
        this.buttonAnalyze.setText("Analyze");
        this.buttonAnalyze.addActionListener(this);
        this.panelAnalProg.add(this.buttonAnalyze);
        this.labelAnalProg.setText("  Progress:  ");
        this.labelAnalProg.setHorizontalAlignment(0);
        this.labelAnalProg.setHorizontalTextPosition(0);
        this.panelAnalProg.add(this.labelAnalProg);
        this.panelAnalProg.add(this.progressAnalysis);
        this.panelAnalyze.add(this.panelAnalProg, "South");
        this.listResults.setEnabled(false);
        this.scrollPane3.setViewportView(this.listResults);
        this.panelAnalyze.add(this.scrollPane3, "Center");
        this.paneOpts.addTab("Analyze", this.panelAnalyze);
        this.add(this.paneOpts, "Center");
        (this.buttonGroup1 = new ButtonGroup()).add(this.radioKnown);
        this.buttonGroup1.add(this.radioUnknown);
        (this.buttonGroup2 = new ButtonGroup()).add(this.radioCrossEnt);
        this.buttonGroup2.add(this.radioLZW);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String command = e.getActionCommand();
        if (command.equals("Exit")) {
            System.exit(-1);
        }
        else if (command.equals("knownauthor")) {
            this.currentAuthor.setText("");
            this.currentAuthor.setEnabled(true);
        }
        else if (command.equals("unknownauthor")) {
            this.currentAuthor.setText("");
            this.currentAuthor.setEnabled(false);
        }
        else if (command.equals("Import")) {
            final String fcomp = new String();
            System.out.println(this.buttonGroup1.getSelection().getActionCommand());
            final int returnVal = this.chooseFile.showOpenDialog(this);
            if (returnVal == 0) {
                final File file = this.chooseFile.getSelectedFile();
                this.driver.addDocument(file.toString(), this.currentAuthor.getText());
                if (!this.currentAuthor.getText().equals("")) {
                    this.listKnown.append(this.currentAuthor.getText() + " - " + file.getName() + "\n");
                }
                else {
                    this.listUnknown.append(file.getName() + "\n");
                }
                System.out.println(file);
            }
        }
        else if (command.equals("Canonicize")) {
            final guiDriver.preprocessEngineDriver pe = this.driver.preprocessEngineDriver(this.canonicizers, this.paneOpts, this.progressCanonicize);
            pe.start();
        }
        else if (command.equals("CreateEventSet")) {
            final guiDriver.createEventSetDriver esd = this.driver.createEventSetDriver(this.esButtonGroup.getSelection().getActionCommand(), this.paneOpts, this.progressEventSet);
            esd.start();
        }
        else if (command.equals("Analyze")) {
            final guiDriver.runStatisticalAnalysisDriver rsad = this.driver.runStatisticalAnalysisDriver(this.buttonGroup2.getSelection().getActionCommand(), this.paneOpts, this.progressAnalysis);
            rsad.start();
            this.listResults.append(rsad.getResults());
        }
        else if (command.equals("demo")) {
            this.loadDemo();
        }
        else {
            System.out.println(command);
        }
    }
    
    public void loadDemo() {
        System.out.println("Need to Demo");
    }
}
