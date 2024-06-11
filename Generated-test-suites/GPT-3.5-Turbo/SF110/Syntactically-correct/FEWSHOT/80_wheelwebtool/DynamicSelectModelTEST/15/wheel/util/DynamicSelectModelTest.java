import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import wheel.StandaloneComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    private DynamicSelectModel selectModel;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private ElExpression translator;
    
    @Before
    public void setUp() {
        collection = mock(ElExpression.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        selectModel = new DynamicSelectModel(collection, label, value);
    }

    @Test
    public void testConstructor() {
        assertEquals(collection, selectModel.collection);
        assertEquals(label, selectModel.label);
        assertEquals(value, selectModel.value);
        assertNull(selectModel.translator);
        assertNull(selectModel.objects);
        assertNull(selectModel.emptyObject);
    }

    @Test
    public void testCollection() {
        String collectionExpression = "collectionExpression";
        selectModel.collection(collectionExpression);
        assertEquals(collectionExpression, selectModel.collection.getExpression());
    }

    @Test
    public void testLabel() {
        String labelExpression = "labelExpression";
        selectModel.label(labelExpression);
        assertEquals(labelExpression, selectModel.label.getExpression());
    }

    @Test
    public void testValue() {
        String valueExpression = "valueExpression";
        selectModel.value(valueExpression);
        assertEquals(valueExpression, selectModel.value.getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        selectModel.enumeration(enumeration);
        assertEquals(enumeration + ".values()", selectModel.collection.getExpression());
        assertEquals("toString()", selectModel.label.getExpression());
        assertEquals("ordinal()", selectModel.value.getExpression());
    }

    @Test
    public void testTranslator() {
        String translatorExpression = "translatorExpression";
        selectModel.translator(translatorExpression);
        assertEquals(translatorExpression, selectModel.translator.getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        selectModel.empty(emptyObject);
        assertEquals(emptyObject, selectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        selectModel.objects = objects;
        assertEquals(2, selectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        Collection<Object> objects = new ArrayList<>();
        Object obj = mock(Object.class);
        objects.add(obj);
        selectModel.objects = objects;
        
        when(label.eval(obj, selectModel.component)).thenReturn("label");
        assertEquals("label", selectModel.getLabel(0));
        
        verify(label).errorMessage("Could not evaluate expression " + label.getExpression() + " to create a label for dynamic ISelectModel. Index was 0");
    }

    @Test
    public void testGetValue() {
        Collection<Object> objects = new ArrayList<>();
        Object obj = mock(Object.class);
        objects.add(obj);
        selectModel.objects = objects;
        
        when(value.eval(obj, selectModel.component)).thenReturn("value");
        assertEquals("value", selectModel.getValue(0));
        
        verify(value).errorMessage("Could not evaluate expression " + value + " to create a value for dynamic ISelectModel. Index was 0");
    }

    @Test
    public void testTranslateValue() {
        Collection<Object> objects = new ArrayList<>();
        Object obj = mock(Object.class);
        objects.add(obj);
        selectModel.objects = objects;
        
        when(value.eval(obj, selectModel.component)).thenReturn("value");
        
        assertEquals(obj, selectModel.translateValue("value"));
        
        verify(value, never()).errorMessage(anyString());
        
        assertEquals(null, selectModel.translateValue("notValue"));
        
        verify(value, times(objects.size())).errorMessage(anyString());
    }

    @Test
    public void testGetObjects() {
        selectModel.collection = collection;
        selectModel.component = mock(Component.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        selectModel.topLevelComponent = topLevelComponent;
        
        selectModel.objects = new ArrayList<>();
        assertEquals(selectModel.objects, selectModel.getObjects());
        
        verify(collection, never()).errorMessage(anyString());
        
        selectModel.objects = null;
        when(collection.eval(topLevelComponent, selectModel.component)).thenReturn(new ArrayList<>());
        selectModel.getObjects();
        
        verify(collection).errorMessage("Could not evaluate expression " + collection + " to create a dynamic ISelectModel.");
        
        selectModel.objects = null;
        when(collection.eval(topLevelComponent, selectModel.component)).thenReturn(null);
        
        try {
            selectModel.getObjects();
        } catch (WheelException e) {
            assertEquals("Expression " + collection + " evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
            assertEquals(selectModel.component, e.getComponent());
        }
        
        selectModel.objects = null;
        when(collection.eval(topLevelComponent, selectModel.component)).thenReturn(new Object());
        
        try {
            selectModel.getObjects();
        } catch (WheelException e) {
            assertEquals("Expression " + collection + " evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
            assertEquals(selectModel.component, e.getComponent());
        }
    }

    @Test
    public void testGetSetComponent() {
        Component component = mock(Component.class);
        selectModel.setComponent(component);
        assertEquals(component, selectModel.component);
        assertEquals(component, selectModel.getComponent());
    }

    @Test
    public void testGetSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        selectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, selectModel.topLevelComponent);
        assertEquals(topLevelComponent, selectModel.getTopLevelComponent());
    }

    @Test
    public void testValidate() {
        selectModel.validate();
        // No exception expected
        
        selectModel.collection = null;
        try {
            selectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }

        selectModel.collection = collection;
        selectModel.label = null;
        try {
            selectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }

        selectModel.label = label;
        selectModel.value = null;
        try {
            selectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
    }

    @Test
    public void testReset() {
        selectModel.objects = new ArrayList<>();
        selectModel.reset();
        assertNull(selectModel.objects);
    }
}