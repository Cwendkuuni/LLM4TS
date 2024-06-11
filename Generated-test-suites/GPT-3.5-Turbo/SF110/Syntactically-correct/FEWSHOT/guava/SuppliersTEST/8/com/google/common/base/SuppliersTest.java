package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Supplier<String> original = Suppliers.ofInstance("Hello");
        Function<String, Integer> fn = new Function<String, Integer>() {
            public Integer apply(String s) {
                return s.length();
            }
        };
        Supplier<Integer> composed = Suppliers.compose(fn, original);
        
        assertEquals(Integer.valueOf(5), composed.get());
        
        Supplier<String> anotherOriginal = Suppliers.ofInstance("World");
        Function<String, Integer> anotherFn = new Function<String, Integer>() {
            public Integer apply(String s) {
                return s.charAt(0);
            }
        };
        Supplier<Integer> anotherComposed = Suppliers.compose(anotherFn, anotherOriginal);
        
        assertEquals(Integer.valueOf('W'), anotherComposed.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = Suppliers.ofInstance("Hello");
        Supplier<String> memoized = Suppliers.memoize(delegate);
        
        assertEquals("Hello", memoized.get());
        assertEquals("Hello", memoized.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
        int count = 0;
        
        public Integer get() {
            count++;
            return count;
        };
    };
    
    Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
    
    assertEquals(Integer.valueOf(1), memoized.get());
    assertEquals(Integer.valueOf(1), memoized.get());
    
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    assertEquals(Integer.valueOf(2), memoized.get());
    assertEquals(Integer.valueOf(2), memoized.get());
}

@Test
public void testOfInstance() {
    Supplier<String> instance = Suppliers.ofInstance("Hello");
    
    assertEquals("Hello", instance.get());
}

@Test
public void testSynchronizedSupplier() {
    Supplier<String> delegate = Suppliers.ofInstance("Hello");
    Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
    
    assertEquals("Hello", synchronizedSupplier.get());
}

@Beta
@Test
public void testSupplierFunction() {
    Supplier<Integer> supplier = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    
    Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
    
    assertEquals(Integer.valueOf(5), function.apply(supplier));
}
}