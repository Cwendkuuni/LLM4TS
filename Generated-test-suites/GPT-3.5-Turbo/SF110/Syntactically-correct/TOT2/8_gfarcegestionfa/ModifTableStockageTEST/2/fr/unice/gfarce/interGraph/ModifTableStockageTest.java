package fr.unice.gfarce.interGraph;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.ajouterColonne("Test", String.class);
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testAjouterLigne() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testSupprimerColonne() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.supprimerColonne(0);
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testSupprimerLigne() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.supprimerLigne(0);
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testSeparerColonne() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testFusionnerColonnes() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testColonneMail() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
        // Add more assertions for edge cases
    }

    @Test
    public void testselectMail() {
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage());
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
        // Add more assertions for edge cases
    }
}