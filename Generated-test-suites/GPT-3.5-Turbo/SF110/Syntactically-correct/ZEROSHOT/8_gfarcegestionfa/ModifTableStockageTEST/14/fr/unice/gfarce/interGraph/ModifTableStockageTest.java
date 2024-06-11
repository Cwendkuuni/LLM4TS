import org.junit.Test;

import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);

        assertEquals("New Column", result.getColumnName(3));
        assertEquals(4, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertTrue(result.getValueAt(0, 0) == ts.getValueAt(0, 0));
        assertTrue(result.getValueAt(1, 1) == ts.getValueAt(1, 1));
        assertTrue(result.getValueAt(2, 2) == ts.getValueAt(2, 2));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterLigne();

        assertEquals(4, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertEquals(ts.getColumnName(2), result.getColumnName(2));
        assertTrue(result.getValueAt(0, 0) == ts.getValueAt(0, 0));
        assertTrue(result.getValueAt(1, 1) == ts.getValueAt(1, 1));
        assertTrue(result.getValueAt(2, 2) == ts.getValueAt(2, 2));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerColonne(2);

        assertEquals(3, result.getColumnCount());
        assertTrue(result.getColumnName(2).equals(ts.getColumnName(3)));
        assertTrue(result.getValueAt(0, 0) == ts.getValueAt(0, 0));
        assertTrue(result.getValueAt(1, 1) == ts.getValueAt(1, 1));
        assertTrue(result.getValueAt(2, 2) == ts.getValueAt(2, 3));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(4, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerLigne(2);

        assertEquals(3, result.getRowCount());
        assertTrue(result.getValueAt(0, 0) == ts.getValueAt(0, 0));
        assertTrue(result.getValueAt(1, 1) == ts.getValueAt(1, 1));
        assertTrue(result.getValueAt(2, 2) == ts.getValueAt(3, 2));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setValueAt("Hello World", 0, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.separerColonne(1);

        assertEquals(4, result.getColumnCount());
        assertEquals("Hello", result.getValueAt(0, 1));
        assertEquals("World", result.getValueAt(0, 2));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.fusionnerColonnes(1);

        assertEquals(3, result.getColumnCount());
        assertEquals("Column1 Column2", result.getColumnName(1));
        assertEquals(ts.getValueAt(0, 0).toString() + " " + ts.getValueAt(0, 1).toString(), result.getValueAt(0, 1));
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.colonneMail();

        assertEquals(4, result.getColumnCount());
        assertEquals(Boolean.class, result.getColumnClass(3));
        assertTrue(result.getValueAt(0, 3).equals(true));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 4);
        ts.setValueAt(true, 0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.selectMail();

        assertEquals(2, result.getRowCount());
        assertEquals(4, result.getColumnCount());
        assertTrue(result.getValueAt(0, 3).equals(true));
    }
}