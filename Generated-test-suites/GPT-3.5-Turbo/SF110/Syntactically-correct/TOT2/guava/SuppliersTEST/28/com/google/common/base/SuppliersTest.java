package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.base.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> function = String::length;
        Supplier<String> supplier = () -> "hello";
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(5), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(10), expiringMemoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("test");
        assertEquals("test", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 15;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(15), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 20;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(20), supplierFunction.apply(supplier));
    }

    @Test
    public void testSupplierCompositionEquals() {
        Function<String, Integer> function = String::length;
        Supplier<String> supplier1 = () -> "hello";
        Supplier<String> supplier2 = () -> "world";
        Supplier<Integer> composedSupplier1 = Suppliers.compose(function, supplier1);
        Supplier<Integer> composedSupplier2 = Suppliers.compose(function, supplier1);
        assertEquals(composedSupplier1, composedSupplier2);
    }

    @Test
    public void testSupplierOfInstanceEquals() {
        Supplier<String> instanceSupplier1 = Suppliers.ofInstance("test");
        Supplier<String> instanceSupplier2 = Suppliers.ofInstance("test");
        assertEquals(instanceSupplier1, instanceSupplier2);
    }

}