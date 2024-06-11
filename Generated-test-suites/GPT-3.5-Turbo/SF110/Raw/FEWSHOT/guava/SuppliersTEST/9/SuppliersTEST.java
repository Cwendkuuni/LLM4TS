package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

@GwtCompatible
public final class SuppliersTest {

    @Test
    public void testCompose(){
        Function<String, Integer> function = new Function<String, Integer>(){
            public Integer apply(String str){
                return Integer.parseInt(str);
            }
        };
        
        Supplier<String> supplier = new Supplier<String>(){
            public String get(){
                return "123";
            }
        };
        
        Supplier<Integer> composed = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(123), composed.get());
    }
    
    @Test
    public void testMemoize(){
        Supplier<String> delegate = new Supplier<String>(){
            public String get(){
                return "memoize";
            }
        };
        
        Supplier<String> memoized = Suppliers.memoize(delegate);
        assertEquals("memoize", memoized.get());
    }
    
    @Test
    public void testMemoizeWithExpiration(){
        Supplier<String> delegate = new Supplier<String>(){
            public String get(){
                return "memoizeWithExpiration";
            }
        };
        
        Supplier<String> memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("memoizeWithExpiration", memoized.get());
    }
    
    @Test
    public void testOfInstance(){
        Supplier<String> instanceSupplier = Suppliers.ofInstance("instance");
        assertEquals("instance", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier(){
        Supplier<String> delegate = new Supplier<String>(){
            public String get(){
                return "synchronizedSupplier";
            }
        };
        
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals("synchronizedSupplier", synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction(){
        Supplier<String> supplier = new Supplier<String>(){
            public String get(){
                return "supplierFunction";
            }
        };
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("supplierFunction", supplierFunction.apply(supplier));
    }
}