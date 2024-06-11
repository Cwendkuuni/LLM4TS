import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    private AttributeModelComparator attributeModelComparator;
    
    @Before
    public void setUp() {
        attributeModelComparator = new AttributeModelComparator();
    }
    
    @Test
    public void testCompare_equalAttributeModels_expectZero() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("A");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_attributeModel1Null_expectNegativeOne() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("b");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_attributeModel2Null_expectPositiveOne() {
        AttributeModel attributeModel1 = new AttributeModel("c");
        AttributeModel attributeModel2 = null;
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_bothAttributeModelsNull_expectZero() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_attributeModelNamesEqual_expectZero() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_attributeModel1NameBeforeA_expectNegativeOne() {
        AttributeModel attributeModel1 = new AttributeModel("A");
        AttributeModel attributeModel2 = new AttributeModel("b");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_attributeModel2NameBeforeA_expectPositiveOne() {
        AttributeModel attributeModel1 = new AttributeModel("c");
        AttributeModel attributeModel2 = new AttributeModel("A");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_attributeModelNamesBeforeA_expectNegativeOne() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("c");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_attributeModelNamesAfterA_expectPositiveOne() {
        AttributeModel attributeModel1 = new AttributeModel("c");
        AttributeModel attributeModel2 = new AttributeModel("b");
        
        int result = attributeModelComparator.compare(attributeModel1, attributeModel2);
        
        assertEquals(1, result);
    }
}