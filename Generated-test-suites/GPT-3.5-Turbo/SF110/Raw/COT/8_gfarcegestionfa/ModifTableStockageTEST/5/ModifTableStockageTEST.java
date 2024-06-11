import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private TableStockage ts;
    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        ts = new TableStockage(2, 2);
        modifTableStockage = new ModifTableStockage(ts);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("Test", String.class);

        assertEquals(3, result.getColumnCount());
        assertEquals("Test", result.getColumnName(2));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();

        assertEquals(3, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);

        assertEquals(1, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(1);

        assertEquals(1, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        ts.setValueAt("12345", 0, 0);
        ts.setColumnClass(0, String.class);

        TableStockage result = modifTableStockage.separerColonne(0);

        assertEquals(3, result.getColumnCount());
        assertEquals("12345", result.getValueAt(0, 0));
        assertEquals("", result.getValueAt(0, 1));
        assertEquals("12345", result.getValueAt(1, 0));
        assertEquals("", result.getValueAt(1, 1));
    }

    @Test
    public void testSeparerColonneWithInvalidType() {
        ts.setValueAt("12345", 0, 0);
        ts.setColumnClass(0, Integer.class);

        TableStockage result = modifTableStockage.separerColonne(0);

        assertSame(ts, result);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);

        assertEquals(1, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(0));
    }

    @Test
    public void testFusionnerColonnesWhenOnlyOneColumn() {
        ts = new TableStockage(2, 1);
        modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.fusionnerColonnes(0);

        assertSame(ts, result);
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();

        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        assertTrue((Boolean) result.getValueAt(0, 2));
        assertTrue((Boolean) result.getValueAt(1, 2));
    }

    @Test
    public void testSelectMail() {
        ts.setColumnClass(2, Boolean.class);
        ts.setValueAt(true, 0, 2);
        ts.setValueAt(false, 1, 2);

        TableStockage result = modifTableStockage.selectMail();

        assertEquals(2, result.getRowCount());
        assertTrue((Boolean) result.getValueAt(0, 2));
    }

}