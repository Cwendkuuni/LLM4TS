import org.junit.*;
import static org.junit.Assert.*;

public class ModifTableStockageTest {
    
    private TableStockage ts;
    private ModifTableStockage mt;
    
    @Before
    public void setUp() {
        ts = new TableStockage(5, 5);
        mt = new ModifTableStockage(ts);
    }
    
    @Test
    public void testAjouterColonne() {
        TableStockage result = mt.ajouterColonne("Test", String.class);
        assertEquals(6, result.getColumnCount());
        assertEquals(6, result.getRowCount());
        assertEquals("Test", result.getColumnName(5));
    }
    
    @Test
    public void testAjouterLigne() {
        TableStockage result = mt.ajouterLigne();
        assertEquals(5, result.getColumnCount());
        assertEquals(6, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        TableStockage result = mt.supprimerColonne(2);
        assertEquals(4, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        TableStockage result = mt.supprimerLigne(3);
        assertEquals(5, result.getColumnCount());
        assertEquals(4, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        TableStockage result = mt.separerColonne(1);
        assertEquals(6, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testFusionnerColonnes() {
       TableStockage result = mt.fusionnerColonnes(1);
        assertEquals(4, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testColonneMail() {
        TableStockage result = mt.colonneMail();
        assertEquals(6, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
    @Test
    public void testSelectMail() {
        TableStockage result = mt.selectMail();
        assertEquals(5, result.getColumnCount());
        assertEquals(5, result.getRowCount());
    }
    
}