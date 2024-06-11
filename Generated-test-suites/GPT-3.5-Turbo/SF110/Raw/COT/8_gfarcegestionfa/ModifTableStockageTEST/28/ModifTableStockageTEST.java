
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterColonne("col1", String.class);

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterLigne();

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerColonne(0);

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerLigne(0);

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.separerColonne(0);

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(1, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.fusionnerColonnes(0);

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.colonneMail();

        // Add assertions to test the functionality of the method
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.selectMail();

        // Add assertions to test the functionality of the method
    }
}

