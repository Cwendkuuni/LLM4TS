import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {
    
    private ModifTableStockage modifTableStockage;
    
    @Before
    public void setUp() {
        // Initialize the ModifTableStockage object with a TableStockage object
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Column 1");
        ts.setColumnName(1, "Column 2");
        ts.setColumnName(2, "Column 3");
        modifTableStockage = new ModifTableStockage(ts);
    }
    
    @After
    public void tearDown() {
        modifTableStockage = null;
    }
    
    @Test
    public void testAjouterColonne() {
        // Test adding a new column
        TableStockage tableS = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals(4, tableS.getColumnCount());
        assertEquals("New Column", tableS.getColumnName(3));
    }
    
    @Test
    public void testAjouterLigne() {
        // Test adding a new row
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(4, tableS.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        // Test removing a column
        TableStockage tableS = modifTableStockage.supprimerColonne(1);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Column 3", tableS.getColumnName(1));
    }
    
    @Test
    public void testSupprimerLigne() {
        // Test removing a row
        TableStockage tableS = modifTableStockage.supprimerLigne(1);
        assertEquals(2, tableS.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        // Test separating a column
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(4, tableS.getColumnCount());
        assertEquals("Column", tableS.getColumnName(0));
        assertEquals("1", tableS.getColumnName(1));
    }
    
    @Test
    public void testFusionnerColonnes() {
        // Test merging two adjacent columns
        TableStockage tableS = modifTableStockage.fusionnerColonnes(0);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Column 1 Column 2", tableS.getColumnName(0));
    }
    
    @Test
    public void testColonneMail() {
        // Test adding a column for sending emails
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(4, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(3));
    }
    
    @Test
    public void testSelectMail() {
        // Test selecting rows to send emails
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(3, tableS.getRowCount());
    }
    
}

