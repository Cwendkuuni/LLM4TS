package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        assertTrue(rs.hasAnnotation("Override"));
        assertFalse(rs.hasAnnotation("Deprecated"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        assertTrue(rs.hasWildcard("java*"));
        assertFalse(rs.hasWildcard("java"));
    }
    
    @Test
    public void testMethodsThatHave() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> methods = rs.methodsThatHave("length").getMethods();
            assertEquals(1, methods.size());
            assertEquals("java.lang.String.length", methods.get(0).toString());
            
            methods = rs.methodsThatHave("substring").getMethods();
            assertEquals(2, methods.size());
            assertTrue(methods.contains(String.class.getMethod("substring", int.class)));
            assertTrue(methods.contains(String.class.getMethod("substring", int.class, int.class)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> methods = rs.methodsThatDontHave("length").getMethods();
            assertEquals(0, methods.size());
            
            methods = rs.methodsThatDontHave("trim").getMethods();
            assertEquals(2, methods.size());
            assertFalse(methods.contains(String.class.getMethod("trim")));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> methods = rs.methodsThatHave("length").and().methodsThatHave("substring").getMethods();
            assertEquals(1, methods.size());
            
            methods = rs.methodsThatHave("trim").and().methodsThatDontHave("substring").getMethods();
            assertEquals(1, methods.size());
            assertEquals("java.lang.String.trim", methods.get(0).toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testOr() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> methods = rs.methodsThatHave("length").or().methodsThatHave("substring").getMethods();
            assertEquals(2, methods.size());
            
            methods = rs.methodsThatHave("length").or().methodsThatHave("trim").getMethods();
            assertEquals(2, methods.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetMethods() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> methods = rs.getMethods();
            assertEquals(71, methods.size());
            assertTrue(methods.contains(String.class.getMethod("length")));
            assertTrue(methods.contains(String.class.getMethod("substring", int.class)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIntersect() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> master = new ArrayList<>();
            master.add(String.class.getMethod("length"));
            master.add(String.class.getMethod("trim"));
            
            List<Method> toadd = new ArrayList<>();
            toadd.add(String.class.getMethod("length"));
            
            rs.intersect(master, toadd);
            
            assertEquals(1, master.size());
            assertTrue(master.contains(String.class.getMethod("length")));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testUnion() {
        ReflectionSearch rs = new ReflectionSearch(String.class);
        try {
            List<Method> master = new ArrayList<>();
            master.add(String.class.getMethod("length"));
            
            List<Method> toadd = new ArrayList<>();
            toadd.add(String.class.getMethod("trim"));
            
            rs.union(master, toadd);
            
            assertEquals(2, master.size());
            assertTrue(master.contains(String.class.getMethod("length")));
            assertTrue(master.contains(String.class.getMethod("trim")));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}