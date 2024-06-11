package fr.unice.gfarce.interGraph;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private TableStockage ts;
    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        ts = new TableStockage(2, 2); // Sample TableStockage object with 2 rows and 2 columns
        ts.setColumnName(0, "Column1");
        ts.setColumnName(1, "Column2");

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ts.setValueAt("Value" + i + "" + j, i, j);
            }
        }

        modifTableStockage = new ModifTableStockage(ts);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("NewCol", Integer.class);
        assertEquals(3, result.getColumnCount());
        assertEquals("NewCol", result.getColumnName(2));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(3, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(0);
        assertEquals(1, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(0);
        assertEquals(1, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertEquals(1, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
    }
}