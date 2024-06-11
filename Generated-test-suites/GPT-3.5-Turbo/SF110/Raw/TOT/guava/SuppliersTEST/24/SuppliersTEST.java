package com.google.common.base;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

  @Test
  public void testCompose_Success() {
    // Typical Use Case
    Function<Integer, String> function = new Function<Integer, String>() {
        public String apply(Integer input) {
            return "Result: " + input.toString();
        }
    };
    Supplier<Integer> supplier = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
    assertEquals("Result: 5", composedSupplier.get());

    // Edge Case
    Function<Integer, String> edgeFunction = new Function<Integer, String>() {
        public String apply(Integer input) {
            if (input % 2 == 0) {
                return "Even";
            } else {
                return "Odd";
            }
        }
    };
    Supplier<Integer> edgeSupplier = new Supplier<Integer>() {
        public Integer get() {
            return 10;
        }
    };
    Supplier<String> edgeComposedSupplier = Suppliers.compose(edgeFunction, edgeSupplier);
    assertEquals("Even", edgeComposedSupplier.get());

    // Error Scenario
    Function<Integer, String> errorFunction = null;
    Supplier<Integer> errorSupplier = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    try {
        Suppliers.compose(errorFunction, errorSupplier);
        fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException ex) {
        // Expected result
    }

    errorFunction = new Function<Integer, String>() {
        public String apply(Integer input) {
            return "";
        }
    };
    Supplier<Integer> nullSupplier = null;
    try {
        Suppliers.compose(errorFunction, nullSupplier);
        fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException ex) {
        // Expected result
    }
  }

  @Test
  public void testMemoize_Success() {
    // Typical Use Case
    Supplier<Integer> delegate = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
    assertEquals(5, (int)memoizedSupplier.get());

    // Edge Case
    Supplier<Integer> edgeDelegate = new Supplier<Integer>() {
        public Integer get() {
            return -10;
        }
    };
    Supplier<Integer> edgeMemoizedSupplier = Suppliers.memoize(edgeDelegate);
    assertEquals(-10, (int)edgeMemoizedSupplier.get());

    // Error Scenario
    Supplier<Integer> errorDelegate = null;
    try {
        Suppliers.memoize(errorDelegate);
        fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException ex) {
        // Expected result
    }
  }
  
  @Test
  public void testMemoizeWithExpiration_Success() throws InterruptedException {
    // Typical Use Case
    Supplier<Integer> delegate = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
    assertEquals(5, (int)memoizedSupplier.get());
    Thread.sleep(2000);
    assertEquals(5, (int)memoizedSupplier.get());
    
    // Edge Case
    Supplier<Integer> edgeDelegate = new Supplier<Integer>() {
        public Integer get() {
            return -10;
        }
    };
    Supplier<Integer> edgeMemoizedSupplier = Suppliers.memoizeWithExpiration(edgeDelegate, 0, TimeUnit.SECONDS);
    assertEquals(-10, (int)edgeMemoizedSupplier.get());

    // Error Scenario
    Supplier<Integer> errorDelegate = null;
    try {
        Suppliers.memoizeWithExpiration(errorDelegate, 1, TimeUnit.SECONDS);
        fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException ex) {
        // Expected result
    }

    Supplier<Integer> negativeDurationDelegate = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    try {
        Suppliers.memoizeWithExpiration(negativeDurationDelegate, -1, TimeUnit.SECONDS);
        fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException ex) {
        // Expected result
    }
  }
  
  @Test
  public void testOfInstance_Success() {
    // Typical Use Case
    Integer instance = 5;
    Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
    assertEquals(5, (int)instanceSupplier.get());

    // Edge Case
    Integer edgeInstance = -10;
    Supplier<Integer> edgeInstanceSupplier = Suppliers.ofInstance(edgeInstance);
    assertEquals(-10, (int)edgeInstanceSupplier.get());

    // Error Scenario
    Integer nullInstance = null;
    Supplier<Integer> nullInstanceSupplier = Suppliers.ofInstance(nullInstance);
    assertEquals(null, nullInstanceSupplier.get());
  }
  
  @Test
  public void testSynchronizedSupplier_Success() {
    // Typical Use Case
    Supplier<Integer> delegate = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
    assertEquals(5, (int)synchronizedSupplier.get());

    // Edge Case
    Supplier<Integer> edgeDelegate = new Supplier<Integer>() {
        public Integer get() {
            return -10;
        }
    };
    Supplier<Integer> edgeSynchronizedSupplier = Suppliers.synchronizedSupplier(edgeDelegate);
    assertEquals(-10, (int)edgeSynchronizedSupplier.get());

    // Error Scenario
    Supplier<Integer> nullDelegate = null;
    try {
        Suppliers.synchronizedSupplier(nullDelegate);
        fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException ex) {
        // Expected result
    }
  }
  
  @Test
  public void testSupplierFunction_Success() {
    // Typical Use Case
    Supplier<Integer> supplier = new Supplier<Integer>() {
        public Integer get() {
            return 5;
        }
    };
    SupplierFunction<Integer> supplierFunction = Suppliers.supplierFunction();
    assertEquals(5, (int)supplierFunction.apply(supplier));

    // Edge Case
    Supplier<Integer> edgeSupplier = new Supplier<Integer>() {
        public Integer get() {
            return -10;
        }
    };
    SupplierFunction<Integer> edgeSupplierFunction = Suppliers.supplierFunction();
    assertEquals(-10, (int)edgeSupplierFunction.apply(edgeSupplier));
  }

}