package fr.unice.gfarce.interGraph;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        modifTableStockage = new ModifTableStockage(new TableStockage());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("Test", String.class);
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.supprimerColonne(0);
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.supprimerLigne(0);
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testSeparerColonne() {
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testColonneMail() {
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }

    @Test
    public void testSelectMail() {
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
        // Add more assertions here for edge cases and exceptions handling
    }
}