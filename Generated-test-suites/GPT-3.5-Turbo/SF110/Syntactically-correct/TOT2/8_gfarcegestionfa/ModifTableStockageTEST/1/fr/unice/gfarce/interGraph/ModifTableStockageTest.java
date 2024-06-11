package fr.unice.gfarce.interGraph;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;

    @Before
    public void setup() {
        modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("test", String.class);
        assertNotNull(result);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
        assertEquals(4, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertNotNull(result);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertNotNull(result);
        assertEquals(2, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
        assertEquals(3, result.getRowCount());
    }

}