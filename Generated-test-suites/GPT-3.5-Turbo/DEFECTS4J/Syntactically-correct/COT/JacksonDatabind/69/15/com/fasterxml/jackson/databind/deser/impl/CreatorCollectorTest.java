
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.setDefaultCreator(null);
        // Verify the default creator is null
        assert(cc.hasDefaultCreator() == false);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addStringCreator(null, false);
        // Verify the existence of a string creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addIntCreator(null, false);
        // Verify the existence of a int creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addLongCreator(null, false);
        // Verify the existence of a long creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addDoubleCreator(null, false);
        // Verify the existence of a double creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addBooleanCreator(null, false);
        // Verify the existence of a boolean creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addDelegatingCreator(null, false, null);
        // Verify the existence of a delegating creator
        assert(cc.hasDelegatingCreator() == true);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addPropertyCreator(null, false, null);
        // Verify the existence of a property creator
        assert(cc.hasPropertyBasedCreator() == true);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.addIncompeteParameter(null);
        // Verify the existence of an incomplete parameter
        assert(cc.hasPropertyBasedCreator() == true);
    }

    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        cc.constructValueInstantiator(null);
        // Verify the type of the value instantiator
        assert(cc.constructValueInstantiator(null) instanceof StdValueInstantiator);
    }
}
