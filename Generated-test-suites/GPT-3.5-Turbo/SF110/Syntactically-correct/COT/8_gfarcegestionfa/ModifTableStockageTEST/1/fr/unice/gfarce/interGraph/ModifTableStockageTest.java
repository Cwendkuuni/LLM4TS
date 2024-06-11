import static org.junit.Assert.*;
import org.junit.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;

    private TableStockage createTableStockage(int rows, int columns) {
        TableStockage tableStockage = new TableStockage(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tableStockage.setValueAt(i + j, i, j);
            }
        }
        return tableStockage;
    }

    @Before
    public void setUp() {
        TableStockage tableStockage = createTableStockage(3, 3);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage newTableStockage = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals("New Column", newTableStockage.getColumnName(3));
        assertEquals("0", newTableStockage.getValueAt(0, 0));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage newTableStockage = modifTableStockage.ajouterLigne();
        assertEquals(4, newTableStockage.getRowCount());
        assertEquals(3, newTableStockage.getColumnCount());
        assertEquals("", newTableStockage.getValueAt(3, 0));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage newTableStockage = modifTableStockage.supprimerColonne(1);
        assertEquals(2, newTableStockage.getColumnCount());
        assertEquals("2", newTableStockage.getValueAt(0, 1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage newTableStockage = modifTableStockage.supprimerLigne(1);
        assertEquals(2, newTableStockage.getRowCount());
        assertEquals("6", newTableStockage.getValueAt(1, 0));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage newTableStockage = modifTableStockage.separerColonne(2);
        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals("8", newTableStockage.getValueAt(0, 2));
        assertEquals("8.0", newTableStockage.getValueAt(0, 3));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage newTableStockage = modifTableStockage.fusionnerColonnes(1);
        assertEquals(2, newTableStockage.getColumnCount());
        assertEquals("1 2", newTableStockage.getColumnName(1));
        assertEquals("0 1", newTableStockage.getValueAt(0, 1));
    }

    @Test
    public void testColonneMail() {
        TableStockage newTableStockage = modifTableStockage.colonneMail();
        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals(Boolean.class, newTableStockage.getColumnClass(3));
        assertEquals(true, newTableStockage.getValueAt(0, 3));
    }

    @Test
    public void testSelectMail() {
        TableStockage newTableStockage = modifTableStockage.selectMail();
        assertEquals(3, newTableStockage.getRowCount());
        assertEquals(3, newTableStockage.getColumnCount());
        assertEquals(0, newTableStockage.getValueAt(0, 0));
        assertEquals(true, newTableStockage.getValueAt(0, 2));
        assertEquals(false, newTableStockage.getValueAt(1, 2));
    }
}