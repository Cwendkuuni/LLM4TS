
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }
    
    // addProperty() tests
    
    @Test
    public void testAddProperty_UniqueNameAndValidType() {
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(type, result.properties.get(name).getType());
        Assert.assertEquals(propertyNode, result.properties.get(name).getPropertyNode());
    }
    
    @Test
    public void testAddProperty_DuplicateName() {
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        recordTypeBuilder.addProperty(name, type, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testAddProperty_NullName() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty(null, type, propertyNode);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testAddProperty_NullType() {
        String name = "property1";
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, null, propertyNode);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testAddProperty_NullPropertyNode() {
        String name = "property1";
        JSType type = new JSType();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, null);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testAddProperty_MultipleProperties() {
        String name1 = "property1";
        JSType type1 = new JSType();
        Node propertyNode1 = new Node();
        
        String name2 = "property2";
        JSType type2 = new JSType();
        Node propertyNode2 = new Node();
        
        recordTypeBuilder.addProperty(name1, type1, propertyNode1);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name2, type2, propertyNode2);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(type2, result.properties.get(name2).getType());
        Assert.assertEquals(propertyNode2, result.properties.get(name2).getPropertyNode());
        // Additional assertions for multiple properties
    }
    
    // build() tests
    
    @Test
    public void testBuild_EmptyRecord() {
        JSType result = recordTypeBuilder.build();
        
        Assert.assertNotNull(result);
        Assert.assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeObjectType());
        // Additional assertions for empty record
    }
    
    @Test
    public void testBuild_RecordWithProperties() {
        String name1 = "property1";
        JSType type1 = new JSType();
        Node propertyNode1 = new Node();
        
        String name2 = "property2";
        JSType type2 = new JSType();
        Node propertyNode2 = new Node();
        
        recordTypeBuilder.addProperty(name1, type1, propertyNode1);
        recordTypeBuilder.addProperty(name2, type2, propertyNode2);
        JSType result = recordTypeBuilder.build();
        
        Assert.assertNotNull(result);
        // Additional assertions for record with properties
    }

}

