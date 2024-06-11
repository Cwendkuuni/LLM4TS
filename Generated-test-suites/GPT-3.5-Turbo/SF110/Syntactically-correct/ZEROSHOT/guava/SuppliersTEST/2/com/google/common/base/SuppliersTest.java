import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.*;

public class SuppliersTest {
  
  @Test
  public void testCompose() {
    Function<Integer, String> function = new Function<Integer, String>() {
      public String apply(Integer input) {
        return input.toString();
      }
    };
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<String> composeSupplier = Suppliers.compose(function, supplier);
    assertEquals("5", composeSupplier.get());
  }
  
  @Test
  public void testMemoize() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> memoizeSupplier = Suppliers.memoize(delegate);
    assertEquals(Integer.valueOf(5), memoizeSupplier.get());
  }
  
  @Test
  public void testMemoizeWithExpiration() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    long duration = 1;
    TimeUnit unit = TimeUnit.SECONDS;
    Supplier<Integer> memoizeWithExpirationSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
    assertEquals(Integer.valueOf(5), memoizeWithExpirationSupplier.get());
  }
  
  @Test
  public void testOfInstance() {
    String instance = "Test";
    Supplier<String> ofInstanceSupplier = Suppliers.ofInstance(instance);
    assertEquals("Test", ofInstanceSupplier.get());
  }
  
  @Test
  public void testSynchronizedSupplier() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
    assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
  }
  
  @Test
  public void testSupplierFunction() {
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
    assertEquals(Integer.valueOf(5), supplierFunction.apply(supplier));
  }
  
  @Test
  public void testSupplierCompositionEquals() {
    Function<Integer, String> function = new Function<Integer, String>() {
      public String apply(Integer input) {
        return input.toString();
      }
    };
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<String> composeSupplier1 = Suppliers.compose(function, supplier);
    Supplier<String> composeSupplier2 = Suppliers.compose(function, supplier);
    assertEquals(composeSupplier1, composeSupplier2);
  }
  
  @Test
  public void testSupplierCompositionHashCode() {
    Function<Integer, String> function = new Function<Integer, String>() {
      public String apply(Integer input) {
        return input.toString();
      }
    };
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<String> composeSupplier = Suppliers.compose(function, supplier);
    assertEquals(function.hashCode() ^ supplier.hashCode(), composeSupplier.hashCode());
  }
  
  @Test
  public void testSupplierCompositionToString() {
    Function<Integer, String> function = new Function<Integer, String>() {
      public String apply(Integer input) {
        return input.toString();
      }
    };
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<String> composeSupplier = Suppliers.compose(function, supplier);
    String expected = "Suppliers.compose(" + function.toString() + ", " + supplier.toString() + ")";
    assertEquals(expected, composeSupplier.toString());
  }
  
  @Test
  public void testMemoizingSupplierEquals() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> memoizeSupplier1 = Suppliers.memoize(delegate);
    Supplier<Integer> memoizeSupplier2 = Suppliers.memoize(delegate);
    assertEquals(memoizeSupplier1, memoizeSupplier2);
  }
  
  @Test
  public void testMemoizingSupplierToString() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> memoizeSupplier = Suppliers.memoize(delegate);
    String expected = "Suppliers.memoize(" + delegate.toString() + ")";
    assertEquals(expected, memoizeSupplier.toString());
  }
  
  @Test
  public void testExpiringMemoizingSupplierEquals() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    long duration = 1;
    TimeUnit unit = TimeUnit.SECONDS;
    Supplier<Integer> memoizeWithExpirationSupplier1 = Suppliers.memoizeWithExpiration(delegate, duration, unit);
    Supplier<Integer> memoizeWithExpirationSupplier2 = Suppliers.memoizeWithExpiration(delegate, duration, unit);
    assertEquals(memoizeWithExpirationSupplier1, memoizeWithExpirationSupplier2);
  }
  
  @Test
  public void testExpiringMemoizingSupplierToString() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    long duration = 1;
    TimeUnit unit = TimeUnit.SECONDS;
    Supplier<Integer> memoizeWithExpirationSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
    String expected = "Suppliers.memoizeWithExpiration(" + delegate.toString() + ", " + duration + ", NANOS)";
    assertEquals(expected, memoizeWithExpirationSupplier.toString());
  }
  
  @Test
  public void testSupplierOfInstanceEquals() {
    String instance = "Test";
    Supplier<String> ofInstanceSupplier1 = Suppliers.ofInstance(instance);
    Supplier<String> ofInstanceSupplier2 = Suppliers.ofInstance(instance);
    assertEquals(ofInstanceSupplier1, ofInstanceSupplier2);
  }
  
  @Test
  public void testSupplierOfInstanceHashCode() {
    String instance = "Test";
    Supplier<String> ofInstanceSupplier = Suppliers.ofInstance(instance);
    assertEquals(instance.hashCode(), ofInstanceSupplier.hashCode());
  }
  
  @Test
  public void testSupplierOfInstanceToString() {
    String instance = "Test";
    Supplier<String> ofInstanceSupplier = Suppliers.ofInstance(instance);
    String expected = "Suppliers.ofInstance(" + instance.toString() + ")";
    assertEquals(expected, ofInstanceSupplier.toString());
  }
  
  @Test
  public void testThreadSafeSupplier() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> threadSafeSupplier = Suppliers.synchronizedSupplier(delegate);
    assertEquals(Integer.valueOf(5), threadSafeSupplier.get());
  }
  
  @Test
  public void testThreadSafeSupplierToString() {
    Supplier<Integer> delegate = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Supplier<Integer> threadSafeSupplier = Suppliers.synchronizedSupplier(delegate);
    String expected = "Suppliers.synchronizedSupplier(" + delegate.toString() + ")";
    assertEquals(expected, threadSafeSupplier.toString());
  }
  
  @Test
  public void testSupplierFunctionApply() {
    Supplier<Integer> supplier = new Supplier<Integer>() {
      public Integer get() {
        return 5;
      }
    };
    Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
    assertEquals(Integer.valueOf(5), supplierFunction.apply(supplier));
  }
  
  @Test
  public void testSupplierFunctionToString() {
    String expected = "Suppliers.supplierFunction()";
    Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
    assertEquals(expected, supplierFunction.toString());
  }
}