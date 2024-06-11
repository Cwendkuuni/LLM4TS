import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {
    private TableStockage tableS;
    private ModifTableStockage modifTableStockage;
    
    @Before
    public void setUp() {
        tableS = new TableStockage(3, 3);
        modifTableStockage = new ModifTableStockage(tableS);
    }
    
    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("Test", String.class);
        
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertEquals("Test", result.getColumnName(3));
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        
        assertEquals(3, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);
        
        assertEquals(2, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(1);
        
        assertEquals(3, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        tableS.setValueAt("Value 1 2", 0, 1);
        tableS.setValueAt(123, 1, 1);
        
        TableStockage result = modifTableStockage.separerColonne(1);
        
        assertEquals(4, result.getColumnCount());
        assertEquals("Value", result.getValueAt(0, 1));
        assertEquals("1 2", result.getValueAt(0, 2));
        assertEquals(123.0, result.getValueAt(1, 1));
    }
    
    @Test
    public void testFusionnerColonnes() {
        tableS.setValueAt("Value 1", 0, 1);
        tableS.setValueAt("Value 2", 0, 2);
        
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        
        assertEquals(2, result.getColumnCount());
        assertEquals("Value 1 Value 2", result.getValueAt(0, 1));
    }
    
    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        
        assertEquals(4, result.getColumnCount());
        assertEquals(Boolean.class, result.getColumnClass(3));
        assertTrue((Boolean)result.getValueAt(0, 3));
    }
    
    @Test
    public void testSelectMail() {
        tableS.setValueAt(true, 0, 3);
        tableS.setValueAt(false, 1, 3);
        tableS.setValueAt(true, 2, 3);
        
        TableStockage result = modifTableStockage.selectMail();
        
        assertEquals(4, result.getColumnCount());
        assertEquals(2, result.getRowCount());
        assertEquals(true, result.getValueAt(0, 3));
        assertEquals(true, result.getValueAt(1, 3));
    }
}