
package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private TableStockage ts;
    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        ts = new TableStockage(3, 3);
        ts.setValueAt("val1", 1, 1);
        ts.setValueAt("val2", 2, 2);
        modifTableStockage = new ModifTableStockage(ts);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("newCol", String.class);
        int expectedColCount = 4;
        assertEquals(expectedColCount, result.getColumnCount());
        assertEquals("newCol", result.getColumnName(3));
        assertEquals("val1", result.getValueAt(1, 1));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        int expectedRowCount = 4;
        assertEquals(expectedRowCount, result.getRowCount());
        assertEquals("val1", result.getValueAt(1, 1));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);
        int expectedColCount = 2;
        assertEquals(expectedColCount, result.getColumnCount());
        assertEquals("val1", result.getValueAt(1, 1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(2);
        int expectedRowCount = 2;
        assertEquals(expectedRowCount, result.getRowCount());
        assertEquals("val1", result.getValueAt(1, 1));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(1);
        int expectedColCount = 4;
        assertEquals(expectedColCount, result.getColumnCount());
        assertEquals("val1", result.getValueAt(1, 1));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        int expectedColCount = 2;
        assertEquals(expectedColCount, result.getColumnCount());
        assertEquals("val1 val2", result.getValueAt(1, 1));
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        int expectedColCount = 4;
        assertEquals(expectedColCount, result.getColumnCount());
        assertEquals(Boolean.TRUE, result.getValueAt(0, 3));
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        int expectedRowCount = 1;
        assertEquals(expectedRowCount, result.getRowCount());
        assertEquals("val1", result.getValueAt(0, 1));
    }
}

