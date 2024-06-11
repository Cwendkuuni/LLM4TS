import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 2);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.ajouterColonne("New Column", String.class);
        assertEquals(3, resultTable.getColumnCount());
        
        // Test edge case
        TableStockage emptyTable = new TableStockage(0, 0);
        ModifTableStockage emptyModifTable = new ModifTableStockage(emptyTable);
        TableStockage emptyResultTable = emptyModifTable.ajouterColonne("Empty Column", String.class);
        assertEquals(1, emptyResultTable.getColumnCount());
    }

    @Test
    public void testAjouterLigne() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 2);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.ajouterLigne();
        assertEquals(3, resultTable.getRowCount());
        
        // Test edge case
        TableStockage noColumnTable = new TableStockage(2, 0);
        ModifTableStockage noColumnModifTable = new ModifTableStockage(noColumnTable);
        TableStockage noColumnResultTable = noColumnModifTable.ajouterLigne();
        assertEquals(0, noColumnResultTable.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.supprimerColonne(1);
        assertEquals(2, resultTable.getColumnCount());
        
        // Test edge case
        TableStockage singleColumnTable = new TableStockage(2, 1);
        ModifTableStockage singleColumnModifTable = new ModifTableStockage(singleColumnTable);
        TableStockage singleColumnResultTable = singleColumnModifTable.supprimerColonne(0);
        assertEquals(0, singleColumnResultTable.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test normal case
        TableStockage initialTable = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.supprimerLigne(1);
        assertEquals(2, resultTable.getRowCount());
        
        // Test edge case
        TableStockage singleRowTable = new TableStockage(1, 2);
        ModifTableStockage singleRowModifTable = new ModifTableStockage(singleRowTable);
        TableStockage singleRowResultTable = singleRowModifTable.supprimerLigne(0);
        assertEquals(0, singleRowResultTable.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 2);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.separerColonne(1);
        assertEquals(3, resultTable.getColumnCount());
        
        // Test edge case
        TableStockage noStringColumnTable = new TableStockage(2, 2);
        ModifTableStockage noStringColumnModifTable = new ModifTableStockage(noStringColumnTable);
        TableStockage noStringColumnResultTable = noStringColumnModifTable.separerColonne(0);
        assertEquals(2, noStringColumnResultTable.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.fusionnerColonnes(1);
        assertEquals(2, resultTable.getColumnCount());
        
        // Test edge case
        TableStockage singleColumnTable = new TableStockage(2, 1);
        ModifTableStockage singleColumnModifTable = new ModifTableStockage(singleColumnTable);
        TableStockage singleColumnResultTable = singleColumnModifTable.fusionnerColonnes(0);
        assertEquals(1, singleColumnResultTable.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 2);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.colonneMail();
        assertEquals(3, resultTable.getColumnCount());
        
        // Test edge case
        TableStockage emptyTable = new TableStockage(0, 0);
        ModifTableStockage emptyModifTable = new ModifTableStockage(emptyTable);
        TableStockage emptyResultTable = emptyModifTable.colonneMail();
        assertEquals(2, emptyResultTable.getColumnCount());
    }

    @Test
    public void testSelectMail() {
        // Test normal case
        TableStockage initialTable = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        TableStockage resultTable = modifTable.selectMail();
        assertEquals(2, resultTable.getRowCount());
        
        // Test edge case
        TableStockage noMailColumnTable = new TableStockage(2, 2);
        ModifTableStockage noMailColumnModifTable = new ModifTableStockage(noMailColumnTable);
        TableStockage noMailColumnResultTable = noMailColumnModifTable.selectMail();
        assertEquals(2, noMailColumnResultTable.getRowCount());
    }
}