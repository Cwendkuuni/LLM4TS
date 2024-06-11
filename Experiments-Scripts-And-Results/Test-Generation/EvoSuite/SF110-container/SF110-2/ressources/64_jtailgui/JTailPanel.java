package fr.pingtimeout.jtail.gui.view;

import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.model.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanel extends JPanel implements Observer
{
    final JTailModel model;
    final JScrollPane scrollPane;
    final JScrollBar verticalScrollBar;
    final JTable dataTable;
    final DefaultTableModel tableModel;
    final int characterHeight;
    private int maxCharacterWidth;
    
    public JTailPanel(final JTailModel model) {
        JTailLogger.debug("Building a new JTailPanel");
        (this.model = model).addObserver(this);
        this.verticalScrollBar = new JScrollBar();
        this.tableModel = new DefaultTableModel(new String[] { "#", "" }, 0) {
            @Override
            public boolean isCellEditable(final int row, final int col) {
                return false;
            }
        };
        this.dataTable = new JTable(this.tableModel);
        (this.scrollPane = new JScrollPane(this.dataTable)).setVerticalScrollBarPolicy(21);
        this.scrollPane.setHorizontalScrollBarPolicy(30);
        this.verticalScrollBar.addAdjustmentListener(new ScrollBarAdjustmentController(this, this.model));
        this.addComponentListener(new JTailSizeController(this, this.model));
        this.setLayout(new BorderLayout());
        this.add(this.scrollPane, "Center");
        this.add(this.verticalScrollBar, "East");
        final FontMetrics metrics = this.dataTable.getFontMetrics(this.dataTable.getFont());
        this.characterHeight = metrics.getHeight() - 1;
        this.maxCharacterWidth = metrics.stringWidth("0") + 2;
    }
    
    public int getCharacterHeight() {
        return this.characterHeight;
    }
    
    public JScrollBar getVerticalScrollBar() {
        return this.verticalScrollBar;
    }
    
    @Override
    public void update(final Observable observable, final Object arg) {
        if (observable == this.model) {
            final JTailModelEvent event = (JTailModelEvent)arg;
            JTailLogger.debug("JTailPanel just received a notification : " + arg);
            if (event.type == JTailModelEvent.Type.RESIZED) {
                JTailLogger.debug("The window has beed resized");
                final BoundedRangeModel rangeModel = this.verticalScrollBar.getModel();
                rangeModel.setValueIsAdjusting(true);
                rangeModel.setMinimum(event.minLine);
                rangeModel.setValue(event.firstLine);
                rangeModel.setExtent(event.nbDisplayedLines);
                rangeModel.setMaximum(event.maxLine);
                rangeModel.setValueIsAdjusting(false);
                if (event.minLine == event.firstLine && event.maxLine == event.nbDisplayedLines) {
                    this.verticalScrollBar.setEnabled(false);
                }
                else if (!this.verticalScrollBar.isEnabled()) {
                    this.verticalScrollBar.setEnabled(true);
                }
                this.updateDataTable(this.model.getLineNumbers(), this.model.getContent());
            }
            else if (event.type == JTailModelEvent.Type.SCROLLED) {
                JTailLogger.debug("The user scrolled the file");
                final String lineNumbers = this.model.getLineNumbers();
                this.updateDataTable(this.model.getLineNumbers(), this.model.getContent());
            }
        }
    }
    
    private void updateDataTable(final String lineNumbers, final String lines) {
        while (this.tableModel.getRowCount() > 0) {
            this.tableModel.removeRow(0);
        }
        final String[] lineNumbersArray = lineNumbers.split("\n");
        final String[] linesArray = lines.split("\n");
        for (int i = 0; i < lineNumbersArray.length; ++i) {
            final String lineNumberTmp = lineNumbersArray[i];
            final String lineTmp = (i < linesArray.length) ? linesArray[i] : "";
            this.tableModel.addRow(new String[] { lineNumberTmp, lineTmp });
        }
        this.dataTable.setAutoResizeMode(3);
        JTailLogger.debug("Resizing column widths");
        JTailLogger.debug("DataTable width : {}", this.dataTable.getWidth());
        JTailLogger.debug("Highest line number : {}", lineNumbersArray[lineNumbersArray.length - 1]);
        final TableColumn numbersColumn = this.dataTable.getColumnModel().getColumn(0);
        final TableColumn linesColumn = this.dataTable.getColumnModel().getColumn(1);
        final int numbersColumnWidth = lineNumbersArray[lineNumbersArray.length - 1].length() * this.maxCharacterWidth;
        final int linesColumnWidth = this.dataTable.getWidth() - numbersColumnWidth;
        JTailLogger.debug("numbersColumnWidth : {}", numbersColumnWidth);
        JTailLogger.debug("linesColumnWidth : {}", linesColumnWidth);
        numbersColumn.setPreferredWidth(numbersColumnWidth);
        linesColumn.setPreferredWidth(linesColumnWidth);
    }
}
