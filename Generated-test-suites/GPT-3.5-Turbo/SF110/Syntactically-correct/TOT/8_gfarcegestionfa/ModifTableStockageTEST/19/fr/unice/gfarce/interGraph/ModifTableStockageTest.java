import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class ModifTableStockageTest {
    
    private TableStockage mockTableStockage;
    private ModifTableStockage modifTableStockage;
    
    @Before
    public void setUp() {
        mockTableStockage = Mockito.mock(TableStockage.class);
        modifTableStockage = new ModifTableStockage(mockTableStockage);
    }
    
    @Test
    public void testAjouterColonne_typicalCase() {
        int initialColCount = mockTableStockage.getColumnCount();
        int initialRowCount = mockTableStockage.getRowCount();
        String newColumnName = "Test Column";
        
        TableStockage result = modifTableStockage.ajouterColonne(newColumnName, String.class);
        
        Mockito.verify(mockTableStockage, Mockito.times(1)).getColumnCount();
        Mockito.verify(mockTableStockage, Mockito.times(1)).getRowCount();
        Mockito.verify(mockTableStockage, Mockito.times(1)).setColumnName(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(mockTableStockage, Mockito.times(initialRowCount)).setValueAt(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mockTableStockage, Mockito.times(1)).setColumnClass(Mockito.anyInt(), Mockito.any());
        
        assertEquals(initialColCount + 1, result.getColumnCount());
        assertEquals(initialRowCount, result.getRowCount());
        assertEquals(newColumnName, result.getColumnName(result.getColumnCount() - 1));
        assertEquals(String.class, result.getColumnClass(result.getColumnCount() - 1));
    }
    
    @Test
    public void testAjouterColonne_edgeCase() {
        int initialColCount = mockTableStockage.getColumnCount();
        int initialRowCount = mockTableStockage.getRowCount();
        String newColumnName = "Test Column";
        
        TableStockage result = modifTableStockage.ajouterColonne(newColumnName, Double.class);
        
        Mockito.verify(mockTableStockage, Mockito.times(1)).getColumnCount();
        Mockito.verify(mockTableStockage, Mockito.times(1)).getRowCount();
        Mockito.verify(mockTableStockage, Mockito.times(1)).setColumnName(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(mockTableStockage, Mockito.times(initialRowCount)).setValueAt(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mockTableStockage, Mockito.times(1)).setColumnClass(Mockito.anyInt(), Mockito.any());
        
        assertEquals(initialColCount + 1, result.getColumnCount());
        assertEquals(initialRowCount, result.getRowCount());
        assertEquals(newColumnName, result.getColumnName(result.getColumnCount() - 1));
        assertEquals(Double.class, result.getColumnClass(result.getColumnCount() - 1));
    }
    
    @Test
    public void testAjouterColonne_errorCase() {
        int initialColCount = mockTableStockage.getColumnCount();
        int initialRowCount = mockTableStockage.getRowCount();
        String newColumnName = "Test Column";
        
        TableStockage result = modifTableStockage.ajouterColonne(newColumnName, Integer.class);
        
        Mockito.verify(mockTableStockage, Mockito.times(1)).getColumnCount();
        Mockito.verify(mockTableStockage, Mockito.times(0)).getRowCount();
        Mockito.verify(mockTableStockage, Mockito.times(0)).setColumnName(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(mockTableStockage, Mockito.times(0)).setValueAt(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mockTableStockage, Mockito.times(0)).setColumnClass(Mockito.anyInt(), Mockito.any());
        
        assertEquals(initialColCount, result.getColumnCount());
        assertEquals(initialRowCount, result.getRowCount());
    }
    
    // Similarly, write test cases for the remaining methods
    
}