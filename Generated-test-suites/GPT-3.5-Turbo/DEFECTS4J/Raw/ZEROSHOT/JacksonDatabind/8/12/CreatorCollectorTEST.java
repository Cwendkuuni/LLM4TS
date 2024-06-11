import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        BeanDescription beanDescription = new BeanDescription();
        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(new AnnotatedWithParams());
        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(new AnnotatedWithParams(), false);
        assertNotNull(creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(new AnnotatedWithParams(), false);
        assertNotNull(creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(new AnnotatedWithParams(), false);
        assertNotNull(creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(), false);
        assertNotNull(creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(), false);
        assertNotNull(creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorProperty[] creatorProperties = new CreatorProperty[]{};
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(), false, creatorProperties);
        assertNotNull(creatorCollector._creators[6]);
        assertNotNull(creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorProperty[] creatorProperties = new CreatorProperty[]{};
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(), false, creatorProperties);
        assertNotNull(creatorCollector._creators[7]);
        assertNotNull(creatorCollector._propertyBasedArgs);
    }
}