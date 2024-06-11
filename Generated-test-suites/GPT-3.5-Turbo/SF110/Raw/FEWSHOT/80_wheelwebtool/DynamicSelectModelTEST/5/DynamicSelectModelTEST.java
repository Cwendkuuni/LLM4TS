import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testConstructorWithParams() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);
        
        DynamicSelectModel model = new DynamicSelectModel(collection, label, value);
        
        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(value, model.value);
    }
    
    @Test
    public void testDefaultConstructor() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        assertNull(model.collection);
        assertNull(model.label);
        assertNull(model.value);
    }
    
    @Test
    public void testCollection() {
        String collection = "collection";
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection(collection);
        
        assertEquals(collection, model.collection.getExpression());
    }
    
    @Test
    public void testLabel() {
        String label = "label";
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.label(label);
        
        assertEquals(label, model.label.getExpression());
    }
    
    @Test
    public void testValue() {
        String value = "value";
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.value(value);
        
        assertEquals(value, model.value.getExpression());
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.enumeration(enumeration);
        
        assertEquals(enumeration + ".values()", model.collection.getExpression());
        assertEquals("toString()", model.label.getExpression());
        assertEquals("ordinal()", model.value.getExpression());
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator(translator);
        
        assertEquals(translator, model.translator.getExpression());
    }
    
    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.empty(emptyObject);
        
        assertEquals(emptyObject, model.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        
        assertEquals(1, model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        ElExpression label = mock(ElExpression.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.label = label;
        model.component = component;
        
        when(label.eval(any(), any())).thenReturn("label");
        
        assertEquals("label", model.getLabel(0));
        verify(label).errorMessage(anyString());
    }
    
    @Test
    public void testGetValue() {
        ElExpression value = mock(ElExpression.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.value = value;
        model.component = component;
        
        when(value.eval(any(), any())).thenReturn("value");
        
        assertEquals("value", model.getValue(0));
        verify(value).errorMessage(anyString());
    }
    
    @Test
    public void testTranslateValue() {
        ElExpression translator = mock(ElExpression.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator = translator;
        model.component = component;
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        
        when(translator.eval(any(), any())).thenReturn("translated");
        when(model.getValue(anyInt())).thenReturn("value");
        
        assertEquals("translated", model.translateValue("value"));
        verify(translator).errorMessage(anyString());
    }
    
    @Test
    public void testGetObjects() {
        ElExpression collection = mock(ElExpression.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = collection;
        model.topLevelComponent = topLevelComponent;
        model.component = component;
        
        Collection objects = new ArrayList<>();
        objects.add(new Object());
        
        when(collection.eval(any(), any())).thenReturn(objects);
        
        assertEquals(objects, model.getObjects());
        verify(collection).errorMessage(anyString());
    }
    
    @Test
    public void testGetObjectsWithArray() {
        ElExpression collection = mock(ElExpression.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = collection;
        model.topLevelComponent = topLevelComponent;
        model.component = component;
        
        Object[] array = new Object[]{new Object()};
        
        when(collection.eval(any(), any())).thenReturn(array);
        
        assertEquals(Arrays.asList(array), model.getObjects());
        verify(collection).errorMessage(anyString());
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNull() {
        ElExpression collection = mock(ElExpression.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = collection;
        model.topLevelComponent = topLevelComponent;
        model.component = component;
        
        when(collection.eval(any(), any())).thenReturn(null);
        
        model.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidType() {
        ElExpression collection = mock(ElExpression.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = collection;
        model.topLevelComponent = topLevelComponent;
        model.component = component;
        
        when(collection.eval(any(), any())).thenReturn(new Object());
        
        model.getObjects();
    }
    
    @Test
    public void testGetObjectsWithEmptyObject() {
        ElExpression collection = mock(ElExpression.class);
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = collection;
        model.topLevelComponent = topLevelComponent;
        model.component = component;
        model.emptyObject = new Object();
        
        Collection objects = new ArrayList<>();
        objects.add(new Object());
        
        when(collection.eval(any(), any())).thenReturn(objects);
        
        assertEquals(objects.size() + 1, model.getObjects().size());
    }
    
    @Test
    public void testGetComponent() {
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = component;
        
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component component = mock(Component.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(component);
        
        assertEquals(component, model.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.topLevelComponent = topLevelComponent;
        
        assertEquals(topLevelComponent, model.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, model.topLevelComponent);
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = mock(ElExpression.class);
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = mock(ElExpression.class);
        model.label = mock(ElExpression.class);
        model.validate();
    }
    
    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>();
        
        model.reset();
        
        assertNull(model.objects);
    }
    
}