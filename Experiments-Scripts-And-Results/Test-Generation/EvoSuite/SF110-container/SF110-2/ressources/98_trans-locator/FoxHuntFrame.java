package Joshua.FoxHunt;

import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

public class FoxHuntFrame extends JFrame implements ActionListener
{
    JTextField theta;
    JTextField lat;
    JTextField lon;
    JTextField zoom;
    JButton add;
    JTable pos;
    FoxTableModel posModel;
    HuntDisplay display;
    JPanel panel1;
    JPanel panel2;
    JPanel panel3;
    JPanel panel;
    Vector points;
    Vector fox;
    
    public FoxHuntFrame() {
        super("Triangulation");
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.points = new Vector();
        this.fox = new Vector();
        this.textPrep();
        this.tablePrep();
        this.displayPrep();
        this.finalPrep();
        final ImageIcon icon = new ImageIcon("icon.gif");
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(3);
        this.getContentPane().add(this.panel, "Center");
        this.pack();
        this.setState(6);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        this.addPoint();
    }
    
    public void addPoint() {
        final int angle = Integer.parseInt(this.theta.getText());
        final double LAT = Double.parseDouble(this.lat.getText());
        final double LON = Double.parseDouble(this.lon.getText());
        final TrigPoint point = new TrigPoint(LAT, LON, angle);
        this.points.add(point);
        this.display.addPoint(point);
        if (this.points.size() > 1) {
            final TrigPoint C = this.points.elementAt(this.points.size() - 2);
            final TrigPoint B = FoxCalc.calcFox(point, C);
            this.fox.add(B);
            this.posModel.fireTableDataChanged();
            this.display.addPoint(B);
        }
    }
    
    private void finalPrep() {
        (this.zoom = new JTextField("1")).addActionListener(this);
        final JLabel zoomL = new JLabel("Zoom");
        final JPanel zoomP = new JPanel(new GridLayout(1, 2));
        zoomP.add(zoomL);
        zoomP.add(this.zoom);
        (this.panel3 = new JPanel(new BorderLayout())).add(this.zoom, "North");
        this.panel3.add(this.display, "Center");
        this.panel = new JPanel(new BorderLayout());
        final JSplitPane controls = new JSplitPane(1, this.panel1, this.panel2);
        final JSplitPane map = new JSplitPane(0, controls, this.panel3);
        map.setDividerLocation(0.25);
        (this.panel = new JPanel(new BorderLayout())).add(map, "Center");
    }
    
    private void textPrep() {
        this.theta = new JTextField(3);
        this.lat = new JTextField(9);
        this.lon = new JTextField(10);
        (this.add = new JButton("Add It!!!")).addActionListener(this);
        final JLabel thetaL = new JLabel("Theta in Degrees");
        final JLabel latL = new JLabel("Lat. dd*mm.mmm");
        final JLabel lonL = new JLabel("lon. ddd*mm.mmm");
        (this.panel1 = new JPanel(new GridLayout(4, 2))).add(thetaL);
        this.panel1.add(this.theta);
        this.panel1.add(latL);
        this.panel1.add(this.lat);
        this.panel1.add(lonL);
        this.panel1.add(this.lon);
        this.panel1.add(this.add);
    }
    
    private void tablePrep() {
        this.posModel = new FoxTableModel();
        this.pos = new JTable(this.posModel);
        final JScrollPane scroll = new JScrollPane(this.pos);
        this.pos.setPreferredScrollableViewportSize(new Dimension(500, 70));
        (this.panel2 = new JPanel(new BorderLayout())).add(scroll, "Center");
    }
    
    private void displayPrep() {
        this.display = new HuntDisplay();
        this.loadLandmarks();
    }
    
    private void loadLandmarks() {
        try {
            final File file = new File("./landmarks.cfg");
            final FileInputStream inFile = new FileInputStream(file);
            final BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
            String input = "";
            int NUM = 0;
            if ((input = in.readLine()) != null) {
                final String num = input.substring(4);
                NUM = Integer.parseInt(num);
            }
            int numAtFile = 0;
            int numAtLat = 0;
            int numAtLon = 0;
            final String[] iconFile = new String[NUM];
            final double[] lat = new double[NUM];
            final double[] lon = new double[NUM];
            while ((input = in.readLine()) != null) {
                if (input.startsWith("FILE")) {
                    iconFile[numAtFile] = input.substring(4);
                    ++numAtFile;
                }
                else if (input.startsWith("LAT")) {
                    lat[numAtLat] = Double.parseDouble(input.substring(4));
                    ++numAtLat;
                }
                else {
                    if (!input.startsWith("LON")) {
                        continue;
                    }
                    lon[numAtLon] = Double.parseDouble(input.substring(4));
                    ++numAtLon;
                }
            }
            for (int a = 0; a < NUM; ++a) {
                this.display.addLandMark(new Landmark(lat[a], lon[a], new ImageIcon(iconFile[a])));
            }
            in.close();
            inFile.close();
        }
        catch (FileNotFoundException e) {
            this.error("Can't Find the file");
        }
        catch (IOException e2) {
            this.error("Error communicating with the file");
        }
    }
    
    private void error(final String text) {
        final Object[] options = { "Abort", "Continue" };
        final int ans = JOptionPane.showOptionDialog(this, text, "Error", 0, 0, null, options, options[0]);
        if (ans == 0 | ans == -1) {
            System.exit(1);
        }
    }
    
    class FoxTableModel extends AbstractTableModel
    {
        String[] colNames;
        
        FoxTableModel() {
            this.colNames = new String[] { "Lattitude", "Longitude" };
        }
        
        @Override
        public int getColumnCount() {
            return this.colNames.length;
        }
        
        @Override
        public int getRowCount() {
            return FoxHuntFrame.this.fox.size();
        }
        
        @Override
        public String getColumnName(final int col) {
            return this.colNames[col];
        }
        
        @Override
        public Object getValueAt(final int row, final int col) {
            final DecimalFormat formatter = new DecimalFormat("###.##############");
            final TrigPoint point = FoxHuntFrame.this.fox.elementAt(row);
            if (col == 0) {
                final String output = formatter.format(point.getY());
                return output;
            }
            final String output = formatter.format(point.getX());
            return output;
        }
        
        @Override
        public Class getColumnClass(final int col) {
            return this.getValueAt(0, col).getClass();
        }
        
        @Override
        public boolean isCellEditable(final int row, final int col) {
            return false;
        }
    }
}
