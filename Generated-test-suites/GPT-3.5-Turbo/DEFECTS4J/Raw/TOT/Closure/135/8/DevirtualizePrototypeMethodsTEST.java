import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

    private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

    @Before
    public void setUp() {
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
    }

    @After
    public void tearDown() {
        devirtualizePrototypeMethods = null;
    }

    @Test
    public void testProcess() {
        // Typical use case
        // Test with a simple class definition and call site
        String input = "function A() {} A.prototype.accumulate = function(value) { this.total += value; return this.total; }; var a = new A(); var total = a.accumulate(2);";
        String expectedOutput = "var accumulate = function(self, value) { self.total += value; return self.total; }; var total = accumulate(a, 2);";
        assertEquals(expectedOutput, devirtualizePrototypeMethods.process(input));

        // Edge case
        // Test with an empty class definition and no call site
        input = "function A() {}";
        expectedOutput = input;
        assertEquals(expectedOutput, devirtualizePrototypeMethods.process(input));

        // Edge case
        // Test with a class definition having multiple methods and call sites
        input = "function B() {} B.prototype.method1 = function(v) { this.value1 = v; return this.value1; }; B.prototype.method2 = function(v) { this.value2 = v; return this.value2; }; var b = new B(); var result1 = b.method1(2); var result2 = b.method2(5);";
        expectedOutput = "var method1 = function(self, v) { self.value1 = v; return self.value1; }; var method2 = function(self, v) { self.value2 = v; return self.value2; }; var b = new B(); var result1 = method1(b, 2); var result2 = method2(b, 5);";
        assertEquals(expectedOutput, devirtualizePrototypeMethods.process(input));

        // Error scenario
        // Test with an invalid class definition
        input = "A.prototype.method = {}";
        expectedOutput = input;
        assertEquals(expectedOutput, devirtualizePrototypeMethods.process(input));

        // Error scenario
        // Test with a call site accessing the property directly
        input = "function A() {} A.prototype.method = function() {}; var a = new A(); a.method();";
        expectedOutput = input;
        assertEquals(expectedOutput, devirtualizePrototypeMethods.process(input));
    }
}