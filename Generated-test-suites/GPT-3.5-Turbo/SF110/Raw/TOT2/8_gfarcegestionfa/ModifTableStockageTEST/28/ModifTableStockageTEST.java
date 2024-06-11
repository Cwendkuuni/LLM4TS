package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a new column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.ajouterColonne("NewColumn", String.class);
        assertEquals(6, newTable.getColumnCount());
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.ajouterLigne();
        assertEquals(6, newTable.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.supprimerColonne(3);
        assertEquals(4, newTable.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.supprimerLigne(2);
        assertEquals(4, newTable.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column into two columns
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.separerColonne(2);
        assertEquals(7, newTable.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns into one
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
        TableStockage newTable = modifTableStockage.fusionnerColonnes(1);
        assertEquals(2, newTable.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test adding a new column for sending emails
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.colonneMail();
        assertEquals(6, newTable.getColumnCount());
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows to send emails
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
        TableStockage newTable = modifTableStockage.colonneMail();
        TableStockage selectedTable = modifTableStockage.selectMail();
        assertEquals(2, selectedTable.getRowCount());
    }
}