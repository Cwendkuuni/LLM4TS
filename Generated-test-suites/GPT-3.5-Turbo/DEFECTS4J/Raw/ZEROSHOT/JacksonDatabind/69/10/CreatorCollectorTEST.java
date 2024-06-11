import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Member;
import java.util.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        BeanDescription beanDescription = mock(BeanDescription.class);
        MapperConfig config = mock(MapperConfig.class);
        creatorCollector = new CreatorCollector(beanDescription, config);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertEquals(creator, creatorCollector._creators[6]);
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertEquals(creator, creatorCollector._creators[7]);
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector._creators[0] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector._creators[6] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector._creators[7] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
}