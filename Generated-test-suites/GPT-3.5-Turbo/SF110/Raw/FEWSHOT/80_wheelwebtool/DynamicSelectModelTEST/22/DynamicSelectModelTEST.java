package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;


public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        collection = mock(ElExpression.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        translator = mock(ElExpression.class);
        objects = mock(Collection.class);
        emptyObject = new Object();
    }
    
    @Test
    public void testConstructor() {
        DynamicSelectModel dsm = new DynamicSelectModel(collection, label, value);
        // Assert statements here
    }
    
    @Test
    public void testCollection() {
        dynamicSelectModel.collection("collection");
        // Assert statements here
    }
    
    @Test
    public void testLabel() {
        dynamicSelectModel.label("label");
        // Assert statements here
    }
    
    @Test
    public void testValue() {
        dynamicSelectModel.value("value");
        // Assert statements here
    }
    
    @Test
    public void testEnumeration() {
        dynamicSelectModel.enumeration("enumeration");
        // Assert statements here
    }
    
    @Test
    public void testTranslator() {
        dynamicSelectModel.translator("translator");
        // Assert statements here
    }
    
    @Test
    public void testEmpty() {
        dynamicSelectModel.empty(emptyObject);
        // Assert statements here
    }
    
    @Test
    public void testGetOptionCount() {
        // Assert statements here
    }
    
    @Test
    public void testGetLabel() {
        // Assert statements here
    }
    
    @Test
    public void testGetValue() {
        // Assert statements here
    }
    
    @Test
    public void testTranslateValue() {
        // Assert statements here
    }
    
    @Test
    public void testGetObjects() {
        // Assert statements here
    }
    
    @Test
    public void testGetComponent() {
        // Assert statements here
    }
    
    @Test
    public void testSetComponent() {
        // Assert statements here
    }
    
    @Test
    public void testGetTopLevelComponent() {
        // Assert statements here
    }
    
    @Test
    public void testSetTopLevelComponent() {
        // Assert statements here
    }
    
    @Test
    public void testValidate() {
        // Assert statements here
    }
    
    @Test
    public void testReset() {
        // Assert statements here
    }
    
}