import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.model.*;
import javax.swing.*;
import javax.swing.table.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(panel.getCharacterHeight(), 0);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        class ObserverImpl implements Observer {
            boolean isCalled = false;
            
            @Override
            public void update(Observable observable, Object arg) {
                isCalled = true;
            }
        }
        
        ObserverImpl observer = new ObserverImpl();
        
        model.addObserver(observer);
        model.notifyObservers();
        
        assertTrue(observer.isCalled);
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        String lineNumbers = "1\n2\n3";
        String lines = "a\nb\nc";
        
        panel.updateDataTable(lineNumbers, lines);
        
        DefaultTableModel tableModel = (DefaultTableModel) panel.dataTable.getModel();
        
        assertEquals(tableModel.getRowCount(), 3);
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            assertEquals(tableModel.getValueAt(i, 0), String.valueOf(i + 1));
            assertEquals(tableModel.getValueAt(i, 1), String.valueOf((char) ('a' + i)));
        }
    }
}