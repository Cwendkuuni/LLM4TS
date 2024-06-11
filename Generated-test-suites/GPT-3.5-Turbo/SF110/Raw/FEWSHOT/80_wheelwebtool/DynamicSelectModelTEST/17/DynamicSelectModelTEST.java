import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.ElExpression;
import wheel.exceptions.WheelException;
import wheel.util.DynamicSelectModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {

    private DynamicSelectModel selectModel;
    private Component component;
    private DynamicSelectModelTest.StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setUp() {
        selectModel = new DynamicSelectModel();
        component = mock(Component.class);
        topLevelComponent = mock(DynamicSelectModelTest.StandaloneComponent.class);
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        collection = mock(ElExpression.class);

        selectModel.setComponent(component);
        selectModel.setTopLevelComponent(topLevelComponent);
        selectModel.label = label;
        selectModel.value = value;
        selectModel.collection = collection;
    }

    @Test
    public void testGetOptionCount() {
        objects = new ArrayList<>();
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));

        when(selectModel.getObjects()).thenReturn(objects);

        assertEquals(3, selectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        objects = new ArrayList<>();
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));

        when(selectModel.getObjects()).thenReturn(objects);

        when(label.eval(objects.get(0), component)).thenReturn("Label1");
        when(label.eval(objects.get(1), component)).thenReturn("Label2");
        when(label.eval(objects.get(2), component)).thenReturn("Label3");

        assertEquals("Label1", selectModel.getLabel(0));
        assertEquals("Label2", selectModel.getLabel(1));
        assertEquals("Label3", selectModel.getLabel(2));
    }

    @Test
    public void testGetValue() {
        objects = new ArrayList<>();
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));

        when(selectModel.getObjects()).thenReturn(objects);

        when(value.eval(objects.get(0), component)).thenReturn("Value1");
        when(value.eval(objects.get(1), component)).thenReturn("Value2");
        when(value.eval(objects.get(2), component)).thenReturn("Value3");

        assertEquals("Value1", selectModel.getValue(0));
        assertEquals("Value2", selectModel.getValue(1));
        assertEquals("Value3", selectModel.getValue(2));
    }

    @Test
    public void testTranslateValue() {
        objects = new ArrayList<>();
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));

        when(selectModel.getObjects()).thenReturn(objects);

        when(value.eval(objects.get(0), component)).thenReturn("Value1");
        when(value.eval(objects.get(1), component)).thenReturn("Value2");
        when(value.eval(objects.get(2), component)).thenReturn("Value3");

        assertNull(selectModel.translateValue("Value4"));
        assertNull(selectModel.translateValue(null));

        when(value.eval(objects.get(0), component)).thenReturn("Value1");
        when(value.eval(objects.get(1), component)).thenReturn("Value2");
        when(value.eval(objects.get(2), component)).thenReturn("Value3");
        when(translator.eval(objects.get(0), component)).thenReturn("Translation1");
        when(translator.eval(objects.get(1), component)).thenReturn("Translation2");
        when(translator.eval(objects.get(2), component)).thenReturn("Translation3");

        selectModel.translator = translator;

        assertEquals("Translation1", selectModel.translateValue("Value1"));
        assertEquals("Translation2", selectModel.translateValue("Value2"));
        assertEquals("Translation3", selectModel.translateValue("Value3"));
    }

    @Test
    public void testGetObjects() {
        objects = new ArrayList<>();
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));
        objects.add(mock(Object.class));

        when(collection.eval(topLevelComponent, component)).thenReturn(objects);

        assertEquals(objects, selectModel.getObjects());

        when(collection.eval(topLevelComponent, component)).thenReturn(null);

        try {
            selectModel.getObjects();
        } catch (WheelException e) {
            assertEquals("Expression " + collection + " evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }

        Object[] array = {mock(Object.class), mock(Object.class), mock(Object.class)};
        when(collection.eval(topLevelComponent, component)).thenReturn(array);

        assertEquals(objects, selectModel.getObjects());

        array = new Object[0];
        when(collection.eval(topLevelComponent, component)).thenReturn(array);

        assertEquals(new LinkedList<Object>(), selectModel.getObjects());

        selectModel.emptyObject = mock(Object.class);

        Collection<Object> newCollection = new LinkedList<>();
        newCollection.add(selectModel.emptyObject);
        newCollection.addAll(objects);

        assertEquals(newCollection, selectModel.getObjects());
    }

    private class StandaloneComponent extends wheel.components.ImplComponent implements wheel.components.StandaloneComponent {
    }

}