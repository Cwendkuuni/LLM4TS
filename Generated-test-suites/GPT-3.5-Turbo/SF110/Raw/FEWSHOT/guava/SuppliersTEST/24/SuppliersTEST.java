
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Suppliers.MemoizingSupplier;
import com.google.common.base.Suppliers.SupplierOfInstance;
import com.google.common.base.Suppliers.ExpiringMemoizingSupplier;
import com.google.common.base.Suppliers.ThreadSafeSupplier;
import com.google.common.base.Suppliers.SupplierComposition;

public class SuppliersTest {

	@Test
	public void testCompose() {
		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return String.valueOf(input);
			}
		};

		Supplier<Integer> supplier = new Supplier<Integer>() {
			@Override
			public Integer get() {
				return Integer.valueOf(10);
			}
		};

		Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
		assertEquals("10", composedSupplier.get());
	}

	@Test
	public void testMemoize() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
		assertEquals(5, memoizedSupplier.get().intValue());
		assertEquals(5, memoizedSupplier.get().intValue());
		assertEquals(5, memoizedSupplier.get().intValue());
	}

	@Test
	public void testMemoizeWithExpiration() throws InterruptedException {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
		assertEquals(5, memoizedSupplier.get().intValue());
		Thread.sleep(200);
		assertEquals(6, memoizedSupplier.get().intValue());
	}

	@Test
	public void testOfInstance() {
		Supplier<String> supplier = Suppliers.ofInstance("Hello");
		assertEquals("Hello", supplier.get());
	}

	@Test
	public void testSynchronizedSupplier() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
		assertTrue(synchronizedSupplier.get() >= 5);
	}

	@Test
	public void testSupplierFunction() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
		assertEquals(5, supplierFunction.apply(supplier).intValue());
	}

	@Test
	public void testSupplierComposition() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return String.valueOf(input);
			}
		};

		SupplierComposition<Integer, String> composedSupplier = new SupplierComposition<Integer, String>(function,
				supplier);
		assertEquals("5", composedSupplier.get());
	}

	@Test
	public void testSupplierCompositionEquals() {
		Supplier<Integer> supplier1 = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Supplier<Integer> supplier2 = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return String.valueOf(input);
			}
		};

		SupplierComposition<Integer, String> composedSupplier1 = new SupplierComposition<Integer, String>(function,
				supplier1);
		SupplierComposition<Integer, String> composedSupplier2 = new SupplierComposition<Integer, String>(function,
				supplier2);

		assertEquals(composedSupplier1, composedSupplier2);
	}

	@Test
	public void testSupplierCompositionHashCode() {
		Supplier<Integer> supplier1 = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Supplier<Integer> supplier2 = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return String.valueOf(input);
			}
		};

		SupplierComposition<Integer, String> composedSupplier1 = new SupplierComposition<Integer, String>(function,
				supplier1);
		SupplierComposition<Integer, String> composedSupplier2 = new SupplierComposition<Integer, String>(function,
				supplier2);

		assertEquals(composedSupplier1.hashCode(), composedSupplier2.hashCode());
	}

	@Test
	public void testSupplierCompositionToString() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return String.valueOf(input);
			}
		};

		SupplierComposition<Integer, String> composedSupplier = new SupplierComposition<Integer, String>(function,
				supplier);
		assertEquals("Suppliers.compose(" + function.toString() + ", " + supplier.toString() + ")",
				composedSupplier.toString());
	}

	@Test
	public void testMemoizingSupplierToString() {
		Supplier<Integer> delegate = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		MemoizingSupplier<Integer> memoizingSupplier = new MemoizingSupplier<Integer>(delegate);
		assertEquals("Suppliers.memoize(" + delegate.toString() + ")", memoizingSupplier.toString());
	}

	@Test
	public void testExpiringMemoizingSupplierToString() {
		Supplier<Integer> delegate = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new ExpiringMemoizingSupplier<Integer>(delegate,
				100, TimeUnit.MILLISECONDS);
		assertEquals("Suppliers.memoizeWithExpiration(" + delegate.toString() + ", 100000000, NANOS)",
				expiringMemoizingSupplier.toString());
	}

	@Test
	public void testSupplierOfInstance() {
		Supplier<String> supplier = new SupplierOfInstance<String>("Hello");
		assertEquals("Hello", supplier.get());
	}

	@Test
	public void testSupplierOfInstanceEquals() {
		SupplierOfInstance<String> supplier1 = new SupplierOfInstance<String>("Hello");
		SupplierOfInstance<String> supplier2 = new SupplierOfInstance<String>("Hello");
		assertEquals(supplier1, supplier2);
	}

	@Test
	public void testSupplierOfInstanceHashCode() {
		SupplierOfInstance<String> supplier1 = new SupplierOfInstance<String>("Hello");
		SupplierOfInstance<String> supplier2 = new SupplierOfInstance<String>("Hello");
		assertEquals(supplier1.hashCode(), supplier2.hashCode());
	}

	@Test
	public void testSupplierOfInstanceToString() {
		SupplierOfInstance<String> supplier = new SupplierOfInstance<String>("Hello");
		assertEquals("Suppliers.ofInstance(Hello)", supplier.toString());
	}

	@Test
	public void testThreadSafeSupplier() {
		Supplier<Integer> supplier = new ThreadSafeSupplier<Integer>(new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		});

		assertTrue(supplier.get() >= 5);
	}

	@Test
	public void testThreadSafeSupplierToString() {
		Supplier<Integer> delegate = new Supplier<Integer>() {
			int value = 5;

			@Override
			public Integer get() {
				return Integer.valueOf(value++);
			}
		};

		ThreadSafeSupplier<Integer> threadSafeSupplier = new ThreadSafeSupplier<Integer>(delegate);
		assertEquals("Suppliers.synchronizedSupplier(" + delegate.toString() + ")", threadSafeSupplier.toString());
	}
}

