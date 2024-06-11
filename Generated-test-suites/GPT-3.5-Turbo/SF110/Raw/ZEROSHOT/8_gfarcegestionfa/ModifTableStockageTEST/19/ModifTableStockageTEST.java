import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage ts;

    @Before
    public void setUp() {
        ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Column1");
        ts.setColumnName(1, "Column2");
        ts.setColumnName(2, "Column3");
        ts.setValueAt("Value1", 0, 0);
        ts.setValueAt("Value2", 1, 1);
        ts.setValueAt("Value3", 2, 2);
        modifTableStockage = new ModifTableStockage(ts);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("Column4", String.class);
        assertEquals(4, result.getColumnCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Column2", result.getColumnName(1));
        assertEquals("Column3", result.getColumnName(2));
        assertEquals("Column4", result.getColumnName(3));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value2", result.getValueAt(1, 1));
        assertEquals("Value3", result.getValueAt(2, 2));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(3, result.getRowCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Column2", result.getColumnName(1));
        assertEquals("Column3", result.getColumnName(2));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value2", result.getValueAt(1, 1));
        assertEquals("Value3", result.getValueAt(2, 2));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertEquals(2, result.getColumnCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Column3", result.getColumnName(1));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value3", result.getValueAt(2, 1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertEquals(2, result.getRowCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Column2", result.getColumnName(1));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value3", result.getValueAt(1, 1));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(4, result.getColumnCount());
        assertEquals("Column2", result.getColumnName(1));
        assertEquals("Column3", result.getColumnName(2));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value2", result.getValueAt(1, 0));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertEquals(2, result.getColumnCount());
        assertEquals("Column2 Column3", result.getColumnName(1));
        assertEquals("Value1 Value3", result.getValueAt(2, 1));
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertEquals(true, result.getValueAt(0, 3));
        assertEquals(true, result.getValueAt(1, 3));
        assertEquals(true, result.getValueAt(2, 3));
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(3, result.getRowCount());
        assertEquals("Column1", result.getColumnName(0));
        assertEquals("Column2", result.getColumnName(1));
        assertEquals("Column3", result.getColumnName(2));
        assertEquals("Value1", result.getValueAt(0, 0));
        assertEquals("Value2", result.getValueAt(1, 1));
        assertEquals("Value3", result.getValueAt(2, 2));
    }
}