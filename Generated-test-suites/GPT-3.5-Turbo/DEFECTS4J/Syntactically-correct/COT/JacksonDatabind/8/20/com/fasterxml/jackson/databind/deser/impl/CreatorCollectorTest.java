
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(null);
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testSetDefaultCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(CreatorCollector.class.getMethod("setDefaultCreator", AnnotatedParameter.class, boolean.class));
        // TODO: Add assertion
    }

    @Test
    public void testAddStringCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(CreatorCollector.class.getMethod("addStringCreator", AnnotatedParameter.class, boolean.class), true);
        // TODO: Add assertion
    }

    @Test
    public void testAddIntCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(CreatorCollector.class.getMethod("addIntCreator", AnnotatedParameter.class, boolean.class), true);
        // TODO: Add assertion
    }

    @Test
    public void testAddLongCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(CreatorCollector.class.getMethod("addLongCreator", AnnotatedParameter.class, boolean.class), true);
        // TODO: Add assertion
    }

    @Test
    public void testAddDoubleCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(CreatorCollector.class.getMethod("addDoubleCreator", AnnotatedParameter.class, boolean.class), true);
        // TODO: Add assertion
    }

    @Test
    public void testAddBooleanCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(CreatorCollector.class.getMethod("addBooleanCreator", AnnotatedParameter.class, boolean.class), true);
        // TODO: Add assertion
    }

    @Test
    public void testAddDelegatingCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(CreatorCollector.class.getMethod("addDelegatingCreator", AnnotatedParameter.class, boolean.class, CreatorProperty[].class), true, new CreatorProperty[0]);
        // TODO: Add assertion
    }

    @Test
    public void testAddPropertyCreator() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(CreatorCollector.class.getMethod("addPropertyCreator", AnnotatedParameter.class, boolean.class, CreatorProperty[].class), true, new CreatorProperty[0]);
        // TODO: Add assertion
    }

    @Test
    public void testAddIncompeteParameter() throws NoSuchMethodException {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(CreatorCollector.class.getMethod("addIncompeteParameter", AnnotatedParameter.class));
        // TODO: Add assertion
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertFalse(creatorCollector.hasDefaultCreator());
    }

}
