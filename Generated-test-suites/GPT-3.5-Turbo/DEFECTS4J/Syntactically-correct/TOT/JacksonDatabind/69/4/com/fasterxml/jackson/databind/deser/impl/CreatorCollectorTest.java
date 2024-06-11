import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector.*;
import com.fasterxml.jackson.databind.introspect.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.setDefaultCreator(creator);
        assertEquals(collector.hasDefaultCreator(), true);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.addStringCreator(creator, true);
        assertEquals(collector.hasDelegatingCreator(), true);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.addIntCreator(creator, true);
        assertEquals(collector.hasPropertyBasedCreator(), true);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.addLongCreator(creator, true);
        assertEquals(collector.hasPropertyBasedCreator(), true);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.addDoubleCreator(creator, true);
        assertEquals(collector.hasPropertyBasedCreator(), true);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        collector.addBooleanCreator(creator, true);
        assertEquals(collector.hasPropertyBasedCreator(), true);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator, true, injectables);
        assertEquals(collector.hasDelegatingCreator(), true);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator, true, properties);
        assertEquals(collector.hasPropertyBasedCreator(), true);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, null, 0, null);
        collector.addIncompeteParameter(parameter);
        assertEquals(collector.hasDefaultCreator(), true);
    }
}