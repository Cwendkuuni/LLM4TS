import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne_NormalCase() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mt = new ModifTableStockage(ts);

        TableStockage result = mt.ajouterColonne("NewColumn", String.class);

        assertEquals(4, result.getColumnCount());
    }

    @Test
    public void testAjouterLigne_NormalCase() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage mt = new ModifTableStockage(ts);

        TableStockage result = mt.ajouterLigne();

        assertEquals(4, result.getRowCount());
    }

    // Add more test cases for other methods...

}