import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private TableStockage ts;
    private ModifTableStockage modifTableStockage;

    @Test
    public void testAjouterColonne() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("newColumn", String.class);
        assertNotNull(result);
    }

    @Test
    public void testAjouterLigne() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertNotNull(result);
    }

    @Test
    public void testSupprimerColonne() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(1);
        assertNotNull(result);
    }

    @Test
    public void testSupprimerLigne() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertNotNull(result);
    }

    @Test
    public void testSeparerColonne() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertNotNull(result);
    }

    @Test
    public void testFusionnerColonnes() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertNotNull(result);
    }

    @Test
    public void testColonneMail() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertNotNull(result);
    }

    @Test
    public void testSelectMail() {
        ts = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertNotNull(result);
    }

}