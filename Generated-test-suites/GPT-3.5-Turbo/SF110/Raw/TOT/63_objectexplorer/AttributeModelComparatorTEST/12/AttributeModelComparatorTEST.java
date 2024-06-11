import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {
    
    private AttributeModelComparator attributeModelComparator;
    private List<AttributeModel> attributeList;
    
    @Before
    public void setup() {
        attributeModelComparator = new AttributeModelComparator();
        attributeList = new ArrayList<AttributeModel>();
    }
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModel attribute1 = new AttributeModel("SameName");
        AttributeModel attribute2 = new AttributeModel("SameName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NullAttributes_ReturnsNegativeOne() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_NullFirstAttribute_ReturnsNegativeOne() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("TestName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_NullSecondAttribute_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("TestName");
        AttributeModel attribute2 = null;
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeNullName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel("TestName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeNullName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("TestName");
        AttributeModel attribute2 = new AttributeModel(null);
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeEmptyName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("");
        AttributeModel attribute2 = new AttributeModel("TestName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeEmptyName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("TestName");
        AttributeModel attribute2 = new AttributeModel("");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeBeforeA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("Ant");
        AttributeModel attribute2 = new AttributeModel("TestName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeBeforeA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("TestName");
        AttributeModel attribute2 = new AttributeModel("Ant");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributeBeforeA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("Ant");
        AttributeModel attribute2 = new AttributeModel("Aardvark");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributeAfterA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("Zebra");
        AttributeModel attribute2 = new AttributeModel("Xylophone");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_EqualIgnoreCaseNames_ReturnsZero() {
        AttributeModel attribute1 = new AttributeModel("testname");
        AttributeModel attribute2 = new AttributeModel("TestName");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_FirstAttributeNullNameAndSecondAfterA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel("Test");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeNullNameAndFirstAfterA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("Test");
        AttributeModel attribute2 = new AttributeModel(null);
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeNullNameAndSecondNullName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel(null);
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeEmptyNameAndSecondAfterA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("");
        AttributeModel attribute2 = new AttributeModel("Test");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeEmptyNameAndFirstAfterA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("Test");
        AttributeModel attribute2 = new AttributeModel("");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeEmptyNameAndSecondEmptyName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("");
        AttributeModel attribute2 = new AttributeModel("");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNull_ReturnsNegativeOne() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeNullAndSecondAttributeNameStartingWithA_ReturnsNegativeOne() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("App");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeNullAndFirstAttributeNameStartingWithA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("App");
        AttributeModel attribute2 = null;
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameStartingWithA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("App");
        AttributeModel attribute2 = new AttributeModel("Apple");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameBeforeA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("Alpha");
        AttributeModel attribute2 = new AttributeModel("App");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameAfterA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("Zebra");
        AttributeModel attribute2 = new AttributeModel("App");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstAttributeNullAndSecondAttributeNameBeforeA_ReturnsOne() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("App");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_SecondAttributeNullAndFirstAttributeNameBeforeA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("App");
        AttributeModel attribute2 = null;
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullName_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel(null);
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameStartingWithA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("Apple");
        AttributeModel attribute2 = new AttributeModel("App");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameBeforeA_ReturnsNegativeOne() {
        AttributeModel attribute1 = new AttributeModel("App");
        AttributeModel attribute2 = new AttributeModel("Alpha");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_FirstAndSecondAttributesNullNameAfterA_ReturnsOne() {
        AttributeModel attribute1 = new AttributeModel("App");
        AttributeModel attribute2 = new AttributeModel("Zebra");
        
        int result = attributeModelComparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
}