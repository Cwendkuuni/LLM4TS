package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComplexTest {
    
    @Test
    public void testConstructorWithParams() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", complex.toString());
    }
    
    @Test
    public void testDefaultConstructor() {
        Complex complex = new Complex();
        assertEquals("0.0 + 0.0i", complex.toString());
    }
    
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        assertEquals(5.0, complex.abs(), 0.0001);
    }
    
    @Test
    public void testPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 1.0);
        complex1.plus(complex2);
        assertEquals("3.0 + 4.0i", complex1.toString());
    }
    
    @Test
    public void testMinus() {
        Complex complex1 = new Complex(3.0, 4.0);
        Complex complex2 = new Complex(1.0, 2.0);
        complex1.minus(complex2);
        assertEquals("2.0 + 2.0i", complex1.toString());
    }
    
    @Test
    public void testTimesComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(2.0, 3.0);
        complex1.times(complex2);
        assertEquals("-4.0 + 7.0i", complex1.toString());
    }
    
    @Test
    public void testTimesAlpha() {
        Complex complex = new Complex(2.0, 3.0);
        complex.times(2.0);
        assertEquals("4.0 + 6.0i", complex.toString());
    }
    
    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        complex.conjugate();
        assertEquals("3.0 - 4.0i", complex.toString());
    }
    
    @Test
    public void testStaticPlus() {
        Complex complex1 = new Complex(2.0, 3.0);
        Complex complex2 = new Complex(1.0, 1.0);
        Complex result = Complex.plus(complex1, complex2);
        assertEquals("3.0 + 4.0i", result.toString());
    }
    
    @Test
    public void testStaticMultiplyComplex() {
        Complex complex1 = new Complex(1.0, 2.0);
        Complex complex2 = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex1, complex2);
        assertEquals("-4.0 + 7.0i", result.toString());
    }
    
    @Test
    public void testStaticMultiplyDouble() {
        Complex complex = new Complex(2.0, 3.0);
        Complex result = Complex.multiply(complex, 2.0);
        assertEquals("4.0 + 6.0i", result.toString());
    }
    
    @Test
    public void testIConstant() {
        Complex i = Complex.I;
        assertEquals("0.0 + 1.0i", i.toString());
    }
}