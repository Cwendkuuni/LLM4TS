package net.sf.javaml.core;

public class Complex
{
    public double im;
    public double re;
    public static final Complex I;
    
    public Complex(final double re, final double im) {
        this.im = 0.0;
        this.re = 0.0;
        this.re = re;
        this.im = im;
    }
    
    public Complex() {
        this(0.0, 0.0);
    }
    
    @Override
    public String toString() {
        return this.re + " + " + this.im + "i";
    }
    
    public double abs() {
        return Math.sqrt(this.re * this.re + this.im * this.im);
    }
    
    public void plus(final Complex b) {
        this.re += b.re;
        this.im += b.re;
    }
    
    public void minus(final Complex b) {
        this.re -= b.re;
        this.im -= b.im;
    }
    
    public void times(final Complex b) {
        final double real = this.re * b.re - this.im * b.im;
        final double imag = this.re * b.im + this.im * b.re;
        this.re = real;
        this.im = imag;
    }
    
    public void times(final double alpha) {
        this.re *= alpha;
        this.im *= alpha;
    }
    
    public void conjugate() {
        this.im = -this.im;
    }
    
    public static Complex plus(final Complex a, final Complex b) {
        final double real = a.re + b.re;
        final double imag = a.im + b.im;
        return new Complex(real, imag);
    }
    
    public static Complex multiply(final Complex a, final double d) {
        return new Complex(a.re * d, a.im * d);
    }
    
    public static Complex multiply(final Complex a, final Complex b) {
        final double real = a.re * b.re - a.im * b.im;
        final double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }
    
    static {
        I = new Complex(0.0, 1.0);
    }
}
