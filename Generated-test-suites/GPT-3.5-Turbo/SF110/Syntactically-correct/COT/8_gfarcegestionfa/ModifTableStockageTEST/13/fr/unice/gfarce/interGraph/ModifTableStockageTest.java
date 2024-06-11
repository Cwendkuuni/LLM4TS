import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterColonne("Col 3", String.class);

        assertEquals(2, result.getColumnCount());
        assertEquals(3, result.getRowCount());
        assertEquals("Col 3", result.getColumnName(2));
        assertEquals(String.class, result.getColumnClass(2));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterLigne();

        assertEquals(2, result.getColumnCount());
        assertEquals(3, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerColonne(1);

        assertEquals(2, result.getColumnCount());
        assertEquals(2, result.getRowCount());
        assertEquals("Col 0", result.getColumnName(0));
        assertEquals("Col 2", result.getColumnName(1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.supprimerLigne(1);

        assertEquals(2, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.separerColonne(1);

        assertEquals(3, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.fusionnerColonnes(1);

        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.colonneMail();

        assertEquals(3, result.getColumnCount());
        assertEquals(Boolean.class, result.getColumnClass(2));
        assertEquals(new Boolean(true), result.getValueAt(0, 2));
        assertEquals(new Boolean(true), result.getValueAt(1, 2));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.selectMail();

        assertEquals(2, result.getColumnCount());
        assertEquals(2, result.getRowCount());
    }
}