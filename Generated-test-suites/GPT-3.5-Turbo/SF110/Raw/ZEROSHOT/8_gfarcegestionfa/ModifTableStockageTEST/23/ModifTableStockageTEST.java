import org.junit.Before;
import org.junit.Test;

import javax.swing.JOptionPane;

import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;

    @Before
    public void setup() {
        TableStockage ts = new TableStockage(2, 2);
        ts.setColumnName(0, "Column 1");
        ts.setColumnName(1, "Column 2");
        ts.setValueAt("Value 1", 0, 0);
        ts.setValueAt("Value 2", 1, 1);
        modifTableStockage = new ModifTableStockage(ts);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
        assertNotNull(result);
        assertEquals(3, result.getColumnCount());
        assertEquals("New Column", result.getColumnName(2));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(modifTableStockage.ts.getValueAt(i, j), result.getValueAt(i, j));
            }
            assertEquals(String.class, result.getColumnClass(2));
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
        assertEquals(2, result.getRowCount());
        assertEquals(modifTableStockage.nbColonnes, result.getColumnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 1) {
                    assertEquals("", result.getValueAt(i, j));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertNotNull(result);
        assertEquals(2, result.getColumnCount());
        assertEquals("Column 1", result.getColumnName(0));
        for (int i = 0; i < 2; i++) {
            assertEquals(modifTableStockage.ts.getValueAt(i, 0), result.getValueAt(i, 0));
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertNotNull(result);
        assertEquals(1, result.getRowCount());
        assertEquals(modifTableStockage.nbColonnes, result.getColumnCount());
        for (int j = 0; j < modifTableStockage.nbColonnes; j++) {
            assertEquals(modifTableStockage.ts.getValueAt(0, j), result.getValueAt(0, j));
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
        assertEquals(1, result.getColumnCount());
        assertEquals("Column 1 Column 2", result.getColumnName(0));
        for (int i = 0; i < 2; i++) {
            assertEquals("Value " + (i + 1), result.getValueAt(i, 0));
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
        assertEquals(modifTableStockage.nbColonnes + 1, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(modifTableStockage.nbColonnes));
        for (int i = 0; i < modifTableStockage.nbLignes - 1; i++) {
            assertEquals(true, result.getValueAt(i, result.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
        assertEquals(modifTableStockage.nbColonnes, result.getColumnCount());
        assertEquals(modifTableStockage.nbLignes, result.getRowCount());
    }
}