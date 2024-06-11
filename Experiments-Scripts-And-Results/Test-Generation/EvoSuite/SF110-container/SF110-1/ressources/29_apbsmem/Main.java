package apbs_mem_gui;

import java.util.*;
import org.jmol.api.*;
import org.jmol.popup.*;
import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;

public class Main
{
    private JMenuBar mainBar;
    private JMenu fileMenu;
    private JMenu focusMenu;
    private JMenu helpMenu;
    private JMenuItem save;
    private JMenuItem open;
    private JMenuItem exit;
    private JMenuItem about;
    private JMenuItem readme;
    private ButtonGroup focusbuttons;
    private JRadioButtonMenuItem setFocus0;
    private JRadioButtonMenuItem setFocus1;
    private JRadioButtonMenuItem setFocus2;
    protected int maxfocus;
    protected int pb;
    private JPanel lpane;
    private JPanel rpane;
    private Container mainPane;
    GridBagConstraints gbc;
    private JButton Run;
    private JButton PQRBrowse1;
    private JButton PQRBrowse2;
    private JButton Preview;
    private JButton RedrawPot;
    protected JCheckBox drawPot;
    protected JFormattedTextField pqrFile1;
    protected JFormattedTextField pqrFile2;
    protected JFormattedTextField gridDimx;
    protected JFormattedTextField gridDimy;
    protected JFormattedTextField gridDimz;
    protected JFormattedTextField gridLen1a;
    protected JFormattedTextField gridLen2a;
    protected JFormattedTextField gridLen3a;
    protected JFormattedTextField gridLen1b;
    protected JFormattedTextField gridLen2b;
    protected JFormattedTextField gridLen3b;
    protected JFormattedTextField gridLen1c;
    protected JFormattedTextField gridLen2c;
    protected JFormattedTextField gridLen3c;
    protected JFormattedTextField countIon1Charge;
    protected JFormattedTextField countIon1Con;
    protected JFormattedTextField countIon1Sz;
    protected JFormattedTextField countIon2Charge;
    protected JFormattedTextField countIon2Con;
    protected JFormattedTextField countIon2Sz;
    protected JFormattedTextField proteinDi;
    protected JFormattedTextField solventDi;
    protected JFormattedTextField membraneDi;
    protected JFormattedTextField srad;
    protected JFormattedTextField sdens;
    protected JFormattedTextField temp;
    protected JFormattedTextField zmem;
    protected JFormattedTextField Lmem;
    protected JFormattedTextField idie;
    protected JFormattedTextField geoFactor1;
    protected JFormattedTextField geoFactor2;
    protected JFormattedTextField geoFactor3;
    protected JFormattedTextField potential;
    protected JFormattedTextField potcontour;
    protected JProgressBar pBar;
    protected JComboBox solMethodCombo;
    protected JComboBox boundaryCondCombo;
    protected JComboBox calcTypeCombo;
    protected JComboBox centerCombo;
    protected JComboBox contourCombo;
    InFile inFile;
    Exec exec;
    FileEditor file;
    boolean hasbeenchanged;
    boolean file_loaded;
    double[] finalenergy;
    double[] memv;
    File ofile;
    String outfilename;
    String[] theEnergy;
    String[] ctypes;
    Scanner fScan;
    PrintWriter outFile;
    private JFrame theWindow;
    protected JmolViewer viewer;
    private JmolAdapter adapter;
    private JmolPopup jmolPopup;
    private static double version;
    private JDialog aboutdialog;
    private JDialog helpdialog;
    private static Main m;
    
    public Main() {
        this.maxfocus = 0;
        this.pb = 0;
        this.gbc = new GridBagConstraints();
        this.exec = new Exec();
        this.file = new FileEditor();
        this.hasbeenchanged = true;
        this.file_loaded = false;
        this.theWindow = new JFrame();
        this.inFile = new InFile();
        (this.theWindow = new JFrame("APBSmem " + Main.version)).setDefaultCloseOperation(3);
        this.mainPane = this.theWindow.getContentPane();
        this.Run = new JButton("Run");
        this.PQRBrowse1 = new JButton("Browse");
        (this.PQRBrowse2 = new JButton("Browse")).setEnabled(false);
        this.Preview = new JButton("Preview");
        this.mainBar = new JMenuBar();
        this.theWindow.setJMenuBar(this.mainBar);
        (this.RedrawPot = new JButton("Redraw")).setEnabled(false);
        this.fileMenu = new JMenu("File");
        this.open = new JMenuItem("Open");
        this.save = new JMenuItem("Save");
        this.exit = new JMenuItem("Exit");
        this.helpMenu = new JMenu("Help");
        this.readme = new JMenuItem("Readme");
        this.about = new JMenuItem("About");
        this.helpMenu.add(this.about);
        this.fileMenu.add(this.open);
        this.fileMenu.add(this.save);
        this.fileMenu.add(this.exit);
        this.focusMenu = new JMenu("Focus");
        this.setFocus0 = new JRadioButtonMenuItem("No focus");
        this.setFocus1 = new JRadioButtonMenuItem("1 focus level");
        this.setFocus2 = new JRadioButtonMenuItem("2 focus levels");
        this.setFocus0.setSelected(true);
        (this.focusbuttons = new ButtonGroup()).add(this.setFocus0);
        this.focusbuttons.add(this.setFocus1);
        this.focusbuttons.add(this.setFocus2);
        this.focusMenu.add(this.setFocus0);
        this.focusMenu.add(this.setFocus1);
        this.focusMenu.add(this.setFocus2);
        this.mainBar.add(this.fileMenu);
        this.mainBar.add(this.focusMenu);
        this.mainBar.add(this.helpMenu);
        this.ctypes = new String[] { "Protein solvation", "Ion solvation", "Gating charge" };
        (this.pBar = new JProgressBar()).setStringPainted(true);
        this.pb = 0;
        (this.gridDimx = new JFormattedTextField(new Integer(97))).setToolTipText("Grid dimension X");
        (this.gridDimy = new JFormattedTextField(new Integer(97))).setToolTipText("Grid dimension Y");
        (this.gridDimz = new JFormattedTextField(new Integer(97))).setToolTipText("Grid dimension Z");
        (this.gridLen1a = new JFormattedTextField(new Double(300.0))).setToolTipText("Grid length X");
        (this.gridLen2a = new JFormattedTextField(new Double(300.0))).setToolTipText("Grid length Y");
        (this.gridLen3a = new JFormattedTextField(new Double(300.0))).setToolTipText("Grid length Z");
        (this.gridLen1b = new JFormattedTextField(new Double(200.0))).setToolTipText("Grid length X");
        (this.gridLen2b = new JFormattedTextField(new Double(200.0))).setToolTipText("Grid length Y");
        (this.gridLen3b = new JFormattedTextField(new Double(200.0))).setToolTipText("Grid length Z");
        (this.gridLen1c = new JFormattedTextField(new Double(100.0))).setToolTipText("Grid length X");
        (this.gridLen2c = new JFormattedTextField(new Double(100.0))).setToolTipText("Grid length Y");
        (this.gridLen3c = new JFormattedTextField(new Double(100.0))).setToolTipText("Grid length Z");
        this.setFocus1Enable(false);
        this.setFocus2Enable(false);
        (this.countIon1Charge = new JFormattedTextField(new Double(1.0))).setToolTipText("Ion 1 CHARGE");
        (this.countIon1Con = new JFormattedTextField(new Double(0.1))).setToolTipText("Ion 1 CONCENTRATION");
        (this.countIon1Sz = new JFormattedTextField(new Double(2.0))).setToolTipText("Ion 1 RADIUS");
        (this.countIon2Charge = new JFormattedTextField(new Double(-1.0))).setToolTipText("Ion 2 CHARGE");
        (this.countIon2Con = new JFormattedTextField(new Double(0.1))).setToolTipText("Ion 2 CONCENTRATION");
        (this.countIon2Sz = new JFormattedTextField(new Double(2.0))).setToolTipText("Ion 2 RADIUS");
        this.proteinDi = new JFormattedTextField(new Double(2.0));
        this.solventDi = new JFormattedTextField(new Double(80.0));
        this.membraneDi = new JFormattedTextField(new Double(2.0));
        (this.pqrFile1 = new JFormattedTextField((Object)new String(""))).setColumns(16);
        (this.pqrFile2 = new JFormattedTextField((Object)new String(""))).setColumns(16);
        this.pqrFile2.setEnabled(false);
        this.srad = new JFormattedTextField(new Double(1.4));
        this.sdens = new JFormattedTextField(new Double(10.0));
        (this.temp = new JFormattedTextField(new Double(298.15))).setToolTipText("Temperature in Kelvin");
        (this.zmem = new JFormattedTextField(new Double(-20.0))).setToolTipText("Protein position in membrane in Angstroms");
        (this.Lmem = new JFormattedTextField(new Double(40.0))).setToolTipText("Membrane length in Angstroms");
        this.idie = new JFormattedTextField(new Double(80.0));
        (this.geoFactor1 = new JFormattedTextField(new Double(0.0))).setToolTipText("Exclusion radius in Angstroms");
        (this.geoFactor2 = new JFormattedTextField(new Double(0.0))).setToolTipText("Exclusion radius in Angstroms");
        (this.geoFactor3 = new JFormattedTextField(new Double(0.0))).setToolTipText("Thickness in Angstroms");
        this.drawPot = new JCheckBox("Draw potential");
        (this.potcontour = new JFormattedTextField((Object)"2")).setToolTipText("Isocontour to draw electrostatic potential (+/-)");
        this.potcontour.setEnabled(false);
        (this.potential = new JFormattedTextField((Object)"50")).setColumns(5);
        this.potential.setToolTipText("Membrane potential in mV");
        this.potential.setEnabled(false);
        (this.solMethodCombo = new JComboBox()).setModel(new DefaultComboBoxModel<String>(new String[] { "lpbe", "npbe" }));
        this.solMethodCombo.setToolTipText("Linearized or Non-linearized Poisson Boltzmann");
        (this.calcTypeCombo = new JComboBox()).setModel(new DefaultComboBoxModel<String>(this.ctypes));
        (this.boundaryCondCombo = new JComboBox()).setModel(new DefaultComboBoxModel<String>(new String[] { "Zero", "SDH", "MDH", "Focus", "Membrane potential" }));
        (this.centerCombo = new JComboBox()).setModel(new DefaultComboBoxModel<String>(new String[] { "Origin", "Molecule 1" }));
        (this.contourCombo = new JComboBox()).setModel(new DefaultComboBoxModel<String>(new String[] { "Membrane-Protein", "Protein-Solvent" }));
        final fileInHandler fhandler = new fileInHandler();
        final menuHandler menuhandler = new menuHandler();
        this.boundaryCondCombo.addActionListener(fhandler);
        this.centerCombo.addActionListener(fhandler);
        this.calcTypeCombo.addActionListener(fhandler);
        this.pqrFile1.addActionListener(fhandler);
        this.pqrFile2.addActionListener(fhandler);
        this.gridDimx.addActionListener(fhandler);
        this.gridDimy.addActionListener(fhandler);
        this.gridDimz.addActionListener(fhandler);
        this.gridLen1a.addActionListener(fhandler);
        this.gridLen2a.addActionListener(fhandler);
        this.gridLen3a.addActionListener(fhandler);
        this.countIon1Charge.addActionListener(fhandler);
        this.countIon1Con.addActionListener(fhandler);
        this.countIon1Sz.addActionListener(fhandler);
        this.countIon2Charge.addActionListener(fhandler);
        this.countIon2Con.addActionListener(fhandler);
        this.countIon2Sz.addActionListener(fhandler);
        this.proteinDi.addActionListener(fhandler);
        this.solventDi.addActionListener(fhandler);
        this.membraneDi.addActionListener(fhandler);
        this.Run.addActionListener(fhandler);
        this.PQRBrowse1.addActionListener(fhandler);
        this.PQRBrowse2.addActionListener(fhandler);
        this.Preview.addActionListener(fhandler);
        this.drawPot.addActionListener(fhandler);
        this.RedrawPot.addActionListener(fhandler);
        this.setFocus0.addActionListener(menuhandler);
        this.setFocus1.addActionListener(menuhandler);
        this.setFocus2.addActionListener(menuhandler);
        this.open.addActionListener(menuhandler);
        this.save.addActionListener(menuhandler);
        this.exit.addActionListener(menuhandler);
        this.about.addActionListener(menuhandler);
        this.readme.addActionListener(menuhandler);
        this.lpane = new JPanel();
        final JmolPanel jmolPanel = new JmolPanel();
        this.aboutdialog = new SimpleAboutDialog(new JFrame());
        this.helpdialog = new SimpleHelpDialog(new JFrame());
        final FormLayout layout = new FormLayout("right:pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref", "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu,pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu,pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu,pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu,pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref");
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        this.lpane.setLayout((LayoutManager)layout);
        final CellConstraints cc = new CellConstraints();
        builder.add((Component)new JLabel("Calculation type"), cc.xy(1, 1));
        builder.add((Component)this.calcTypeCombo, cc.xyw(3, 1, 5));
        builder.add((Component)new JLabel("PQR File 1"), cc.xy(1, 3));
        builder.add((Component)this.pqrFile1, cc.xyw(3, 3, 3));
        builder.add((Component)this.PQRBrowse1, cc.xy(7, 3));
        builder.add((Component)new JLabel("PQR File 2"), cc.xy(1, 5));
        builder.add((Component)this.pqrFile2, cc.xyw(3, 5, 3));
        builder.add((Component)this.PQRBrowse2, cc.xy(7, 5));
        builder.add((Component)new JLabel("Grid dimensions"), cc.xy(1, 7));
        builder.add((Component)this.gridDimx, cc.xy(3, 7));
        builder.add((Component)this.gridDimy, cc.xy(5, 7));
        builder.add((Component)this.gridDimz, cc.xy(7, 7));
        builder.add((Component)new JLabel("Grid lengths"), cc.xy(1, 9));
        builder.add((Component)this.gridLen1a, cc.xy(3, 9));
        builder.add((Component)this.gridLen2a, cc.xy(5, 9));
        builder.add((Component)this.gridLen3a, cc.xy(7, 9));
        builder.add((Component)new JLabel(""), cc.xy(1, 11));
        builder.add((Component)this.gridLen1b, cc.xy(3, 11));
        builder.add((Component)this.gridLen2b, cc.xy(5, 11));
        builder.add((Component)this.gridLen3b, cc.xy(7, 11));
        builder.add((Component)new JLabel(""), cc.xy(1, 13));
        builder.add((Component)this.gridLen1c, cc.xy(3, 13));
        builder.add((Component)this.gridLen2c, cc.xy(5, 13));
        builder.add((Component)this.gridLen3c, cc.xy(7, 13));
        builder.add((Component)new JLabel("Counter-ions"), cc.xy(1, 15));
        builder.add((Component)this.countIon1Charge, cc.xy(3, 15));
        builder.add((Component)this.countIon1Con, cc.xy(5, 15));
        builder.add((Component)this.countIon1Sz, cc.xy(7, 15));
        builder.add((Component)this.countIon2Charge, cc.xy(3, 17));
        builder.add((Component)this.countIon2Con, cc.xy(5, 17));
        builder.add((Component)this.countIon2Sz, cc.xy(7, 17));
        builder.addSeparator("Dielectric constants", cc.xyw(1, 21, 7));
        builder.add((Component)new JLabel("Protein"), cc.xy(1, 23));
        builder.add((Component)this.proteinDi, cc.xy(3, 23));
        builder.add((Component)new JLabel("Solvent"), cc.xy(5, 23));
        builder.add((Component)this.solventDi, cc.xy(7, 23));
        builder.add((Component)new JLabel("Membrane"), cc.xy(1, 25));
        builder.add((Component)this.membraneDi, cc.xy(3, 25));
        builder.add((Component)new JLabel("Head group"), cc.xy(5, 25));
        builder.add((Component)this.idie, cc.xy(7, 25));
        builder.addSeparator("Geometry settings", cc.xyw(1, 27, 7));
        builder.add((Component)new JLabel("Membrane thickness"), cc.xy(1, 29));
        builder.add((Component)this.Lmem, cc.xy(3, 29));
        builder.add((Component)new JLabel("Upper exclusion"), cc.xy(5, 29));
        builder.add((Component)this.geoFactor1, cc.xy(7, 29));
        builder.add((Component)new JLabel("Head group thickness"), cc.xy(1, 31));
        builder.add((Component)this.geoFactor3, cc.xy(3, 31));
        builder.add((Component)new JLabel("Lower exclusion"), cc.xy(5, 31));
        builder.add((Component)this.geoFactor2, cc.xy(7, 31));
        builder.add((Component)new JLabel("Membrane bottom z-pos"), cc.xy(1, 33));
        builder.add((Component)this.zmem, cc.xy(3, 33));
        builder.addSeparator("Other parameters", cc.xyw(1, 35, 7));
        builder.add((Component)new JLabel("Solution method"), cc.xy(1, 37));
        builder.add((Component)this.solMethodCombo, cc.xy(3, 37));
        builder.add((Component)new JLabel("Temperature"), cc.xy(5, 37));
        builder.add((Component)this.temp, cc.xy(7, 37));
        builder.add((Component)new JLabel("Boundary conditions"), cc.xy(1, 39));
        builder.add((Component)this.boundaryCondCombo, cc.xy(3, 39));
        builder.add((Component)new JLabel("Membrane potential"), cc.xy(5, 39));
        builder.add((Component)this.potential, cc.xy(7, 39));
        builder.add((Component)new JLabel("Solvent radius"), cc.xy(1, 41));
        builder.add((Component)this.srad, cc.xy(3, 41));
        builder.add((Component)new JLabel("Sphere density"), cc.xy(5, 41));
        builder.add((Component)this.sdens, cc.xy(7, 41));
        builder.add((Component)new JLabel("Isosurface contour"), cc.xy(1, 43));
        builder.add((Component)this.contourCombo, cc.xy(3, 43));
        builder.add((Component)new JLabel("Grid center"), cc.xy(5, 43));
        builder.add((Component)this.centerCombo, cc.xy(7, 43));
        builder.add((Component)this.drawPot, cc.xy(3, 45));
        builder.add((Component)this.potcontour, cc.xy(5, 45));
        builder.add((Component)this.RedrawPot, cc.xy(7, 45));
        builder.add((Component)new JLabel(""), cc.xy(1, 47));
        builder.add((Component)this.Preview, cc.xy(3, 47));
        builder.add((Component)this.Run, cc.xy(5, 47));
        final JSplitPane sp = new JSplitPane(1, builder.getPanel(), null);
        sp.setOneTouchExpandable(true);
        sp.setBorder(null);
        this.mainPane.add(sp, "Center");
        final JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(this.pBar, "West");
        this.mainPane.add(bottom, "South");
        (this.viewer = jmolPanel.getViewer()).evalString("zap;frank off;");
        final File logo = new File("apbsmem.jvxl");
        if (logo.exists()) {
            this.viewer.evalString("isoSurface \"apbsmem.jvxl\";zoom 200;");
        }
        this.theWindow.pack();
        jmolPanel.setPreferredSize(new Dimension(300, 300));
        sp.setRightComponent(jmolPanel);
        this.theWindow.pack();
        this.theWindow.setVisible(true);
        this.dependencyCheck();
    }
    
    public static void main(final String[] args) {
        Main.m = new Main();
    }
    
    public Double getIsocontour() {
        if (this.contourCombo.getSelectedIndex() == 0) {
            if (Double.valueOf(this.inFile.getMdie()) < Double.valueOf(this.inFile.getProteinDi())) {
                return Double.valueOf(this.inFile.getMdie()) + 0.001;
            }
            return Double.valueOf(this.inFile.getProteinDi()) + 0.001;
        }
        else {
            if (Double.valueOf(this.inFile.getProteinDi()) < Double.valueOf(this.inFile.getSolventDi())) {
                return Double.valueOf(this.inFile.getProteinDi()) + 0.001;
            }
            return Double.valueOf(this.inFile.getSolventDi()) + 0.001;
        }
    }
    
    public void setMaxFocus(final int mf) {
        this.maxfocus = mf;
        this.inFile.setMaxFocus(mf);
        if (this.maxfocus == 0) {
            if (this.boundaryCondCombo.getSelectedIndex() == 3) {
                this.boundaryCondCombo.setSelectedIndex(0);
            }
            this.setFocus1Enable(false);
            this.setFocus2Enable(false);
            this.setFocus0.setSelected(true);
        }
        else if (this.maxfocus == 1) {
            this.boundaryCondCombo.setSelectedIndex(3);
            this.setFocus1Enable(true);
            this.setFocus2Enable(false);
            this.setFocus1.setSelected(true);
        }
        else {
            this.boundaryCondCombo.setSelectedIndex(3);
            this.setFocus1Enable(true);
            this.setFocus2Enable(true);
            this.setFocus2.setSelected(true);
        }
    }
    
    public void setFocus1Enable(final boolean v) {
        this.gridLen1b.setEnabled(v);
        this.gridLen2b.setEnabled(v);
        this.gridLen3b.setEnabled(v);
    }
    
    public void setFocus2Enable(final boolean v) {
        this.gridLen1c.setEnabled(v);
        this.gridLen2c.setEnabled(v);
        this.gridLen3c.setEnabled(v);
    }
    
    public void SaveToFile(final boolean firstcall) {
        if (this.ofile != null) {
            try {
                if (firstcall) {
                    final File outFile2 = new File(this.ofile.getPath() + ".dummy.in");
                    outFile2.delete();
                    this.outFile = new PrintWriter(new FileOutputStream(outFile2));
                    System.out.println("HEY: " + outFile2.getPath());
                }
                else {
                    final File outFile2 = new File(this.ofile.getPath() + ".solv.in");
                    outFile2.delete();
                    this.outFile = new PrintWriter(new FileOutputStream(outFile2));
                }
            }
            catch (Exception e1) {
                System.out.println("SaveToFile exception: " + e1.toString());
            }
            final String tempIn = this.inFile.toString(firstcall);
            final String[] tempInSplit = tempIn.split("\n");
            for (int i = 0; i < tempInSplit.length; ++i) {
                this.outFile.println(tempInSplit[i]);
            }
            this.outFile.close();
        }
    }
    
    public boolean SaveData() {
        if ((this.pqrFile1.getText().equals("") || this.pqrFile1.getText() == null || this.gridDimx.getText().equals("") || this.gridDimx.getText() == null || this.gridDimy.getText().equals("") || this.gridDimy.getText() == null || this.gridDimz.getText().equals("") || this.gridDimz.getText() == null || this.gridLen1a.getText().equals("") || this.gridLen1a.getText() == null || this.gridLen2a.getText().equals("") || this.gridLen2a.getText() == null || this.gridLen3a.getText().equals("") || this.gridLen3a.getText() == null || this.countIon1Charge.getText().equals("") || this.countIon1Charge.getText() == null || this.countIon1Con.getText().equals("") || this.countIon1Con.getText() == null || this.countIon1Sz.getText().equals("") || this.countIon1Sz.getText() == null || this.countIon2Charge.getText().equals("") || this.countIon2Charge.getText() == null || this.countIon2Con.getText().equals("") || this.countIon2Con.getText() == null || this.countIon2Sz.getText().equals("") || this.countIon2Sz.getText() == null || this.proteinDi.getText().equals("") || this.proteinDi.getText() == null || this.solventDi.getText().equals("") || this.solventDi.getText() == null || this.srad.getText().equals("") || this.srad.getText() == null || this.sdens.getText().equals("") || this.sdens.getText() == null || this.temp.getText().equals("") || this.temp.getText() == null) && this.maxfocus > 0 && (this.gridLen1b.getText().equals("") || this.gridLen1b.getText() == null || this.gridLen2b.getText().equals("") || this.gridLen2b.getText() == null || this.gridLen3b.getText().equals("") || this.gridLen3b.getText() == null) && this.maxfocus > 1 && (this.gridLen1c.getText().equals("") || this.gridLen1c.getText() == null || this.gridLen2c.getText().equals("") || this.gridLen2c.getText() == null || this.gridLen3c.getText().equals("") || this.gridLen3c.getText() == null) && this.file_loaded) {
            JOptionPane.showMessageDialog(null, "Fill in all values first", "", 0);
            return false;
        }
        this.inFile.setMol1(this.pqrFile1.getText());
        this.inFile.setMol2(this.pqrFile2.getText());
        this.inFile.setDime(new String(this.gridDimx.getText() + " " + this.gridDimy.getText() + " " + this.gridDimz.getText()));
        this.inFile.setGlen(new String(this.gridLen1a.getText() + " " + this.gridLen2a.getText() + " " + this.gridLen3a.getText()), 0);
        if (this.maxfocus > 0) {
            this.inFile.setGlen(new String(this.gridLen1b.getText() + " " + this.gridLen2b.getText() + " " + this.gridLen3b.getText()), 1);
        }
        if (this.maxfocus == 2) {
            this.inFile.setGlen(new String(this.gridLen1c.getText() + " " + this.gridLen2c.getText() + " " + this.gridLen3c.getText()), 2);
        }
        this.inFile.setIon1Charge(this.countIon1Charge.getText());
        this.inFile.setIon1Conc(this.countIon1Con.getText());
        this.inFile.setIon1Radius(this.countIon1Sz.getText());
        this.inFile.setIon2Charge(this.countIon2Charge.getText());
        this.inFile.setIon2Conc(this.countIon2Con.getText());
        this.inFile.setIon2Radius(this.countIon2Sz.getText());
        this.inFile.setProteinDi(new String(this.proteinDi.getText()));
        this.inFile.setSolventDi(new String(this.solventDi.getText()));
        this.inFile.setSrad(this.srad.getText());
        this.inFile.setSdens(this.sdens.getText());
        this.inFile.setTemp(this.temp.getText());
        this.inFile.setType(this.calcTypeCombo.getSelectedIndex());
        this.inFile.setPotential(this.potential.getText());
        this.inFile.setLmem(this.Lmem.getText());
        this.inFile.setZmem(this.zmem.getText());
        this.inFile.setMdie(this.membraneDi.getText());
        this.inFile.setIdie(this.idie.getText());
        this.inFile.setGeo1(this.geoFactor1.getText());
        this.inFile.setGeo2(this.geoFactor2.getText());
        this.inFile.setGeo3(this.geoFactor3.getText());
        this.inFile.setDrawPot(this.drawPot.isSelected());
        if (this.boundaryCondCombo.getSelectedIndex() == 2) {
            this.inFile.setBoundCond(new String("mdh"));
        }
        else if (this.boundaryCondCombo.getSelectedIndex() == 1) {
            this.inFile.setBoundCond(new String("sdh"));
        }
        else if (this.boundaryCondCombo.getSelectedIndex() == 0) {
            this.inFile.setBoundCond(new String("zero"));
        }
        if (this.solMethodCombo.getSelectedIndex() == 1) {
            this.inFile.setSolMethod(new String("npbe"));
        }
        else if (this.solMethodCombo.getSelectedIndex() == 0) {
            this.inFile.setSolMethod(new String("lpbe"));
        }
        if (this.centerCombo.getSelectedIndex() == 0) {
            this.inFile.setCenter(new String("0 0 0"));
        }
        else if (this.centerCombo.getSelectedIndex() == 1) {
            this.inFile.setCenter(new String("mol 1"));
        }
        return true;
    }
    
    private void LoadData(final String[] thedata, final int loadfocus) {
        boolean countion1 = true;
        for (int i = 0; i < thedata.length; ++i) {
            if (thedata[i].contains("mol pqr ")) {
                if (thedata[i - 1].contains("mol pqr ")) {
                    if (thedata[i - 2].contains("mol pqr ")) {
                        this.calcTypeCombo.setSelectedIndex(1);
                    }
                    else {
                        String f_temp = thedata[i].substring(thedata[i].indexOf("mol pqr ") + 8, thedata[i].length());
                        if (f_temp.startsWith("\"")) {
                            f_temp = f_temp.substring(1, f_temp.length() - 1);
                        }
                        this.pqrFile2.setText(f_temp);
                        this.calcTypeCombo.setSelectedIndex(2);
                    }
                }
                else {
                    String f_temp = thedata[i].substring(thedata[i].indexOf("mol pqr ") + 8, thedata[i].length());
                    if (f_temp.startsWith("\"")) {
                        f_temp = f_temp.substring(1, f_temp.length() - 1);
                    }
                    this.pqrFile1.setText(f_temp);
                    this.viewer.openFile(this.pqrFile1.getText());
                }
            }
            else if (thedata[i].contains("dime ")) {
                final String[] tempdime = thedata[i].substring(thedata[i].indexOf("dime ") + 5, thedata[i].length()).split(" ");
                if (tempdime.length > 0) {
                    this.gridDimx.setText(tempdime[0]);
                }
                else {
                    this.gridDimx.setText("");
                }
                if (tempdime.length > 1) {
                    this.gridDimy.setText(tempdime[1]);
                }
                else {
                    this.gridDimy.setText("");
                }
                if (tempdime.length > 2) {
                    this.gridDimz.setText(tempdime[2]);
                }
                else {
                    this.gridDimz.setText("");
                }
            }
            else if (thedata[i].contains("glen ")) {
                final String[] tempglen = thedata[i].substring(thedata[i].indexOf("glen ") + 5, thedata[i].length()).split(" ");
                if (loadfocus == 0) {
                    if (tempglen.length > 0) {
                        this.gridLen1a.setText(tempglen[0]);
                    }
                    else {
                        this.gridLen1a.setText("");
                    }
                    if (tempglen.length > 1) {
                        this.gridLen2a.setText(tempglen[1]);
                    }
                    else {
                        this.gridLen2a.setText("");
                    }
                    if (tempglen.length > 2) {
                        this.gridLen3a.setText(tempglen[2]);
                    }
                    else {
                        this.gridLen3a.setText("");
                    }
                }
                else if (loadfocus == 1) {
                    if (tempglen.length > 0) {
                        this.gridLen1b.setText(tempglen[0]);
                    }
                    else {
                        this.gridLen1b.setText("");
                    }
                    if (tempglen.length > 1) {
                        this.gridLen2b.setText(tempglen[1]);
                    }
                    else {
                        this.gridLen2b.setText("");
                    }
                    if (tempglen.length > 2) {
                        this.gridLen3b.setText(tempglen[2]);
                    }
                    else {
                        this.gridLen3b.setText("");
                    }
                }
                else {
                    if (tempglen.length > 0) {
                        this.gridLen1c.setText(tempglen[0]);
                    }
                    else {
                        this.gridLen1c.setText("");
                    }
                    if (tempglen.length > 1) {
                        this.gridLen2c.setText(tempglen[1]);
                    }
                    else {
                        this.gridLen2c.setText("");
                    }
                    if (tempglen.length > 2) {
                        this.gridLen3c.setText(tempglen[2]);
                    }
                    else {
                        this.gridLen3c.setText("");
                    }
                }
            }
            else if (thedata[i].contains("ion ")) {
                final String[] tempion = thedata[i].substring(thedata[i].indexOf("ion ") + 4, thedata[i].length()).split(" ");
                if (countion1) {
                    if (tempion.length > 0) {
                        this.countIon1Charge.setText(tempion[0]);
                    }
                    else {
                        this.countIon1Charge.setText("");
                    }
                    if (tempion.length > 1) {
                        this.countIon1Con.setText(tempion[1]);
                    }
                    else {
                        this.countIon1Con.setText("");
                    }
                    if (tempion.length > 2) {
                        this.countIon1Sz.setText(tempion[2]);
                    }
                    else {
                        this.countIon1Sz.setText("");
                    }
                    countion1 = false;
                }
                else {
                    if (tempion.length > 0) {
                        this.countIon2Charge.setText(tempion[0]);
                    }
                    else {
                        this.countIon2Charge.setText("");
                    }
                    if (tempion.length > 1) {
                        this.countIon2Con.setText(tempion[1]);
                    }
                    else {
                        this.countIon2Con.setText("");
                    }
                    if (tempion.length > 2) {
                        this.countIon2Sz.setText(tempion[2]);
                    }
                    else {
                        this.countIon2Sz.setText("");
                    }
                }
            }
            else if (thedata[i].contains("pdie ")) {
                this.proteinDi.setText(thedata[i].substring(thedata[i].indexOf("pdie ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("sdie ")) {
                this.solventDi.setText(thedata[i].substring(thedata[i].indexOf("sdie ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("srad ")) {
                this.srad.setText(thedata[i].substring(thedata[i].indexOf("srad ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("sdens ")) {
                this.sdens.setText(thedata[i].substring(thedata[i].indexOf("sdens ") + 6, thedata[i].length()));
            }
            else if (thedata[i].contains("temp ")) {
                this.temp.setText(thedata[i].substring(thedata[i].indexOf("temp ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("mdie ")) {
                this.membraneDi.setText(thedata[i].substring(thedata[i].indexOf("mdie ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("zmem ")) {
                this.zmem.setText(thedata[i].substring(thedata[i].indexOf("zmem ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("lmem ")) {
                this.Lmem.setText(thedata[i].substring(thedata[i].indexOf("lmem ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("memv ")) {
                this.potential.setText(thedata[i].substring(thedata[i].indexOf("memv ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("idie ")) {
                this.idie.setText(thedata[i].substring(thedata[i].indexOf("idie ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("geo1 ")) {
                System.out.println(thedata[i]);
                this.geoFactor1.setText(thedata[i].substring(thedata[i].indexOf("geo1 ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("geo2 ")) {
                this.geoFactor2.setText(thedata[i].substring(thedata[i].indexOf("geo2 ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("geo3 ")) {
                this.geoFactor3.setText(thedata[i].substring(thedata[i].indexOf("geo3 ") + 5, thedata[i].length()));
            }
            else if (thedata[i].contains("gcent ")) {
                final String loadctr = thedata[i].substring(thedata[i].indexOf("gcent ") + 6, thedata[i].length());
                if (loadctr.equals("0 0 0")) {
                    this.centerCombo.setSelectedIndex(0);
                }
                else if (loadctr.equals("mol 1")) {
                    this.centerCombo.setSelectedIndex(1);
                }
            }
            else if (thedata[i].contains("lpbe")) {
                this.solMethodCombo.setSelectedIndex(0);
            }
            else if (thedata[i].contains("npbe")) {
                this.solMethodCombo.setSelectedIndex(1);
            }
            else if (thedata[i].contains("bcfl ")) {
                final String loadbcfl = thedata[i].substring(thedata[i].indexOf("bcfl ") + 5, thedata[i].length());
                if (loadbcfl.equals("zero")) {
                    this.boundaryCondCombo.setSelectedIndex(0);
                }
                else if (loadbcfl.equals("sdh")) {
                    this.boundaryCondCombo.setSelectedIndex(1);
                }
                else if (loadbcfl.equals("mdh")) {
                    this.boundaryCondCombo.setSelectedIndex(2);
                }
                else if (loadbcfl.equals("focus")) {
                    this.boundaryCondCombo.setSelectedIndex(3);
                }
                else if (loadbcfl.equals("memv")) {
                    this.boundaryCondCombo.setSelectedIndex(4);
                }
            }
        }
    }
    
    protected void log(final String msg) {
        try {
            this.outFile = new PrintWriter(new FileOutputStream(new File(this.ofile.getPath() + ".log"), true));
        }
        catch (Exception e1) {
            System.out.print(e1.toString());
        }
        this.outFile.println(msg);
        this.outFile.close();
    }
    
    private boolean OpenFile() {
        final File currentDir = new File(".");
        final JFileChooser fc = new JFileChooser(currentDir);
        final int returnVal = fc.showOpenDialog(this.theWindow);
        if (returnVal == 0) {
            this.ofile = fc.getSelectedFile();
            return this.file_loaded = true;
        }
        return false;
    }
    
    private boolean SaveDialog(final String savetitle) {
        final File currentDir = new File(".");
        final JFileChooser fc = new JFileChooser(currentDir);
        fc.setDialogTitle(savetitle);
        final int returnVal = fc.showSaveDialog(this.theWindow);
        if (returnVal == 0) {
            (this.ofile = fc.getSelectedFile()).mkdir();
            this.log(this.ofile.getParent() + "/" + this.ofile.getName() + "/" + this.ofile.getName());
            this.ofile = new File(this.ofile.getParent() + "/" + this.ofile.getName() + "/" + this.ofile.getName());
            return this.file_loaded = true;
        }
        return false;
    }
    
    private void deleteFiles(final String directory, final String extension) {
        final ExtensionFilter filter = new ExtensionFilter(extension);
        final File dir = new File(directory);
        final String[] list = dir.list(filter);
        if (list.length == 0) {
            return;
        }
        System.out.println("Cleaning up current working directory...");
        for (int i = 0; i < list.length; ++i) {
            final File todelete = new File(directory, list[i]);
            System.out.println(todelete + "  deleted: " + todelete.delete());
        }
    }
    
    public void copyFiles(final String src, final String dst, final String extension) {
        final ExtensionFilter filter = new ExtensionFilter(extension);
        final File dir = new File(src);
        final File[] list = dir.listFiles(filter);
        if (list.length == 0) {
            return;
        }
        try {
            for (int i = 0; i < list.length; ++i) {
                final InputStream in = new FileInputStream(list[i]);
                final OutputStream out = new FileOutputStream(new File(dst + "/" + list[i].getName()));
                final byte[] buf = new byte[1024];
                int len = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                System.out.print(list[i].getPath() + " copied to " + dst);
            }
        }
        catch (Exception fe) {
            System.out.println(fe);
        }
    }
    
    private void dependencyCheck() {
        String dependencyError = "";
        final File[] requiredFiles = new File[3];
        String osExt = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            osExt = ".exe";
        }
        requiredFiles[0] = new File("draw_membrane4" + osExt);
        requiredFiles[1] = new File("pull_comps" + osExt);
        requiredFiles[2] = new File("total_charge_off_2" + osExt);
        for (final File i : requiredFiles) {
            if (!i.exists()) {
                dependencyError = dependencyError + i.getName() + " was not found in your APBSmem directory.\n";
            }
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(new String[0]);
            if (System.getProperty("os.name").startsWith("Windows")) {
                pb = new ProcessBuilder(new String[] { "apbs.exe", "--version" });
            }
            else {
                pb = new ProcessBuilder(new String[] { "apbs", "--version" });
            }
            final Process pp = pb.start();
            final int exitVal = pp.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            dependencyError += "Could not run APBS. Make sure it is installed and located on your path.\n";
        }
        if (!dependencyError.equals("")) {
            JOptionPane.showMessageDialog(null, "One or more errors were found during startup:\n" + dependencyError + "\nFor more information please see the APBSmem manual.", "Error", 0);
        }
    }
    
    static {
        Main.version = 1.04;
    }
    
    private class menuHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == Main.this.setFocus0) {
                Main.this.setMaxFocus(0);
            }
            if (e.getSource() == Main.this.setFocus1) {
                Main.this.setMaxFocus(1);
            }
            if (e.getSource() == Main.this.setFocus2) {
                Main.this.setMaxFocus(2);
            }
            else if (e.getSource() == Main.this.about) {
                Main.this.aboutdialog.show();
            }
            else if (e.getSource() == Main.this.readme) {
                Main.this.helpdialog.show();
            }
            else if (e.getSource() == Main.this.open) {
                if (Main.this.OpenFile()) {
                    try {
                        Main.this.fScan = new Scanner(new FileInputStream(Main.this.ofile));
                    }
                    catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                    Main.this.calcTypeCombo.setSelectedIndex(0);
                    StringBuffer theData = new StringBuffer();
                    int loadfocus = -1;
                    while (Main.this.fScan.hasNextLine()) {
                        final String nextline = Main.this.fScan.nextLine();
                        if (nextline.contains("elec name")) {
                            if (loadfocus > -1 && nextline.endsWith("0")) {
                                break;
                            }
                            ++loadfocus;
                        }
                        else if (nextline.contains("end")) {
                            Main.this.SaveData();
                            Main.this.maxfocus = loadfocus;
                            final String temp = theData.toString();
                            final String[] data = temp.split("\n");
                            Main.this.LoadData(data, loadfocus);
                            theData = new StringBuffer();
                        }
                        else {
                            theData.append(nextline + "\n");
                        }
                    }
                    Main.this.fScan.close();
                    Main.this.setMaxFocus(Main.this.maxfocus);
                }
            }
            else if (e.getSource() == Main.this.save) {
                if (Main.this.SaveData() && Main.this.SaveDialog("Save settings to file")) {
                    Main.this.SaveToFile(true);
                    Main.this.hasbeenchanged = false;
                }
            }
            else if (e.getSource() == Main.this.exit) {
                if (Main.this.hasbeenchanged) {
                    int answer;
                    if (Main.this.file_loaded) {
                        answer = JOptionPane.showConfirmDialog(null, "Do you want to save changes to " + Main.this.ofile.getName() + "?", "", 0);
                    }
                    else {
                        answer = JOptionPane.showConfirmDialog(null, "Do you want to save your settings?", "", 0);
                    }
                    if (answer == 0) {
                        if (!Main.this.file_loaded) {
                            Main.this.SaveDialog("Save settings to file");
                        }
                        Main.this.SaveToFile(true);
                    }
                }
                final File curdir = new File(".");
                Main.this.deleteFiles(curdir.getPath(), ".dx");
                System.exit(0);
            }
        }
    }
    
    private class fileInHandler implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == Main.this.Preview) {
                if (Main.this.pqrFile1.getText() == "") {
                    JOptionPane.showMessageDialog(null, "Please select the PQR file", "Error", -1);
                    return;
                }
                final File tfile = new File(Main.this.pqrFile1.getText());
                if (!tfile.exists()) {
                    JOptionPane.showMessageDialog(null, "PQR file not found", "Error", -1);
                    return;
                }
                final Integer t1 = Integer.parseInt(Main.this.gridDimx.getText());
                final Integer t2 = Integer.parseInt(Main.this.gridDimy.getText());
                final Integer t3 = Integer.parseInt(Main.this.gridDimz.getText());
                Main.this.gridDimx.setText("65");
                Main.this.gridDimy.setText("65");
                Main.this.gridDimz.setText("65");
                Main.this.SaveData();
                Main.this.ofile = new File("./preview_temp");
                if (Main.this.calcTypeCombo.getSelectedIndex() == 1 && !Main.this.pqrFile2.getText().equals("")) {
                    Main.this.viewer.evalString("load APPEND \"" + Main.this.pqrFile2.getText() + "\"");
                    Main.this.viewer.evalString("frame *;cpk off;wireframe off;cartoon on;color green;select 2.0;color purple;spacefill 100%");
                }
                final Thread pT = new Thread(new Run(Main.m, Main.this.inFile, Main.this.ofile, Main.this.viewer, Main.this.pBar, Main.this.drawPot.isSelected(), Main.this.potcontour.getText(), true));
                pT.start();
                Main.this.ofile.delete();
                Main.this.gridDimx.setText(t1.toString());
                Main.this.gridDimy.setText(t2.toString());
                Main.this.gridDimz.setText(t3.toString());
                Main.this.pBar.setValue(0);
            }
            else if (e.getSource() == Main.this.Run) {
                if (Main.this.pqrFile1.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select the PQR file for the protein.", "Error", -1);
                    return;
                }
                final File tfile2 = new File(Main.this.pqrFile1.getText());
                if (!tfile2.exists()) {
                    System.out.println("Protein PQR file not found.");
                    return;
                }
                System.out.println("Focus: " + Main.this.maxfocus + " " + Main.this.inFile.getMaxfocus());
                final File curdir = new File(".");
                Main.this.deleteFiles(curdir.getPath(), ".dx");
                if (Main.this.SaveData() && Main.this.SaveDialog("Enter a name for the output directory before running")) {
                    if (Main.this.calcTypeCombo.getSelectedIndex() == 1) {
                        Main.this.viewer.evalString("load FILES \"" + Main.this.pqrFile1.getText() + "\" \"" + Main.this.pqrFile2.getText() + "\"");
                        try {
                            final BufferedWriter prot1 = new BufferedWriter(new FileWriter(Main.this.pqrFile1.getText() + ".withion.pqr", false));
                            final BufferedReader in1 = new BufferedReader(new FileReader(Main.this.pqrFile1.getText()));
                            final BufferedReader in2 = new BufferedReader(new FileReader(Main.this.pqrFile2.getText()));
                            String inputline;
                            while ((inputline = in1.readLine()) != null) {
                                prot1.write(inputline);
                                prot1.newLine();
                            }
                            while ((inputline = in2.readLine()) != null) {
                                prot1.write(inputline);
                                prot1.newLine();
                            }
                            prot1.flush();
                            prot1.close();
                            in1.close();
                            in2.close();
                        }
                        catch (Exception e2) {
                            System.out.println(e2);
                        }
                        Main.this.inFile.setMol3(Main.this.pqrFile1.getText() + ".withion.pqr");
                        Main.this.viewer.evalString("load APPEND \"" + Main.this.pqrFile2.getText() + "\"");
                        Main.this.viewer.evalString("frame *;cpk off;wireframe off;cartoons on;color green;select 2.0;color purple;spacefill 100%");
                    }
                    else if (Main.this.calcTypeCombo.getSelectedIndex() == 2) {
                        Main.this.exec.callChargeOff(Main.this.pqrFile1.getText());
                        Main.this.exec.callChargeOff(Main.this.pqrFile2.getText());
                    }
                    final Thread t4 = new Thread(new Run(Main.m, Main.this.inFile, Main.this.ofile, Main.this.viewer, Main.this.pBar, Main.this.drawPot.isSelected(), Main.this.potcontour.getText(), false));
                    t4.start();
                    if (Main.this.drawPot.isEnabled()) {
                        Main.this.RedrawPot.setEnabled(true);
                    }
                }
                Main.this.pBar.setValue(0);
            }
            else if (e.getSource() == Main.this.PQRBrowse1) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                final EFileFilter filter = new EFileFilter("pqr");
                filter.setDescription("PQR files");
                fc.addChoosableFileFilter(filter);
                final int returnVal = fc.showOpenDialog(Main.this.theWindow);
                if (returnVal == 0) {
                    Main.this.pqrFile1.setText(fc.getSelectedFile().getPath());
                    Main.this.viewer.openFile(Main.this.pqrFile1.getText());
                }
            }
            else if (e.getSource() == Main.this.PQRBrowse2) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                final EFileFilter filter = new EFileFilter("pqr");
                filter.setDescription("PQR files");
                fc.addChoosableFileFilter(filter);
                final int returnVal = fc.showOpenDialog(Main.this.theWindow);
                if (returnVal == 0) {
                    Main.this.pqrFile2.setText(fc.getSelectedFile().getPath());
                }
            }
            else if (e.getSource() == Main.this.boundaryCondCombo) {
                if (Main.this.calcTypeCombo.getSelectedIndex() == 2) {
                    Main.this.boundaryCondCombo.setSelectedIndex(4);
                    Main.this.potential.setEnabled(true);
                }
                else {
                    if (Main.this.boundaryCondCombo.getSelectedIndex() == 4) {
                        Main.this.boundaryCondCombo.setSelectedIndex(0);
                        Main.this.potential.setEnabled(false);
                    }
                    if (Main.this.boundaryCondCombo.getSelectedIndex() == 3 && Main.this.maxfocus == 0) {
                        final Object[] choices = { "0", "1", "2" };
                        final String temp = (String)JOptionPane.showInputDialog(null, "How many focus levels do you want to set?", "Focus levels", 3, null, choices, choices[0]);
                        Main.this.setMaxFocus(Integer.parseInt(temp));
                    }
                }
            }
            else if (e.getSource() == Main.this.calcTypeCombo) {
                if (Main.this.calcTypeCombo.getSelectedIndex() == 0) {
                    Main.this.pqrFile2.setEnabled(false);
                    Main.this.PQRBrowse2.setEnabled(false);
                    Main.this.pqrFile1.setToolTipText("PQR file of protein in membrane");
                    if (Main.this.boundaryCondCombo.getSelectedIndex() == 4) {
                        Main.this.boundaryCondCombo.setSelectedIndex(0);
                    }
                }
                else if (Main.this.calcTypeCombo.getSelectedIndex() == 1) {
                    Main.this.pqrFile2.setEnabled(true);
                    Main.this.PQRBrowse2.setEnabled(true);
                    Main.this.pqrFile1.setToolTipText("PQR file of protein");
                    Main.this.pqrFile2.setToolTipText("PQR file of ion only");
                    if (Main.this.boundaryCondCombo.getSelectedIndex() == 4) {
                        Main.this.boundaryCondCombo.setSelectedIndex(0);
                    }
                }
                else {
                    Main.this.pqrFile2.setEnabled(true);
                    Main.this.PQRBrowse2.setEnabled(true);
                    Main.this.pqrFile1.setToolTipText("PQR file of gating conformation 1");
                    Main.this.pqrFile2.setToolTipText("PQR file of gating conformation 2");
                    Main.this.boundaryCondCombo.setSelectedIndex(4);
                    Main.this.potential.setEnabled(true);
                }
            }
            else if (e.getSource() == Main.this.drawPot) {
                Main.this.potcontour.setEnabled(Main.this.drawPot.isSelected());
            }
            else if (e.getSource() == Main.this.RedrawPot) {
                Main.this.viewer.evalString("isosurface delete");
                if (Main.this.drawPot.isSelected()) {
                    Main.this.viewer.evalString("isosurface " + Main.this.getIsocontour() + " \"dielx_" + (Main.this.maxfocus + 1) + "m.dx\"; color isosurface white translucent");
                    Main.this.viewer.evalString("isosurface potpos " + Main.this.potcontour.getText() + " \"pot_1.dx\"; color isoSurface red translucent");
                    Main.this.viewer.evalString("isosurface potneg -" + Main.this.potcontour.getText() + " \"pot_1.dx\"; color isoSurface blue translucent");
                }
            }
        }
    }
    
    public class ExtensionFilter implements FilenameFilter
    {
        private String extension;
        
        public ExtensionFilter(final String extension) {
            this.extension = extension;
        }
        
        @Override
        public boolean accept(final File dir, final String name) {
            return name.endsWith(this.extension);
        }
    }
    
    public class SimpleAboutDialog extends JDialog
    {
        public SimpleAboutDialog(final JFrame parent) {
            super(parent, "About APBSmem", true);
            final Box b = Box.createVerticalBox();
            b.add(Box.createGlue());
            b.add(new JLabel("APBSmem v" + Main.version));
            b.add(new JLabel("Grabe Lab"));
            b.add(new JLabel("University of Pittsburgh"));
            b.add(new JLabel("http://mgrabe1.bio.pitt.edu/apbsmem/"));
            b.add(Box.createGlue());
            this.getContentPane().add(b, "Center");
            final JPanel p = new JPanel();
            final JButton ok = new JButton("OK");
            p.add(ok);
            this.getContentPane().add(p, "South");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent evt) {
                    SimpleAboutDialog.this.setVisible(false);
                }
            });
            this.setSize(300, 200);
        }
    }
    
    public class SimpleHelpDialog extends JDialog
    {
        public SimpleHelpDialog(final JFrame parent) {
            super(parent, "APBSmem Help", true);
            final JScrollPane jsp = new JScrollPane();
            try {
                final JEditorPane jep = new JEditorPane("http://mgrabe1.bio.pitt.edu/apbsmem/");
                jep.setEditable(false);
                jsp.getViewport().add(jep, "Center");
            }
            catch (Exception e) {
                System.out.println("couldnt load editorpane");
            }
            final JButton ok = new JButton("OK");
            jsp.validate();
            jsp.add(ok);
            this.getContentPane().add(jsp, "South");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent evt) {
                    SimpleHelpDialog.this.setVisible(false);
                }
            });
            this.setSize(300, 200);
        }
    }
}
