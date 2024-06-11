
package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheBuilderSpecTest {
	
	@Test
	public void testParse() {
		CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,concurrencyLevel=4,expireAfterWrite=5m,expireAfterAccess=10m,refreshAfterWrite=2h,recordStats");
		assertEquals(10, spec.initialCapacity.intValue());
		assertEquals(100, spec.maximumSize.longValue());
		assertNull(spec.maximumWeight);
		assertEquals(4, spec.concurrencyLevel.intValue());
		assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
		assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
		assertEquals(true, spec.recordStats);
		assertEquals(5, spec.writeExpirationDuration);
		assertEquals(5, spec.accessExpirationDuration);
		assertEquals(2, spec.refreshDuration);
		assertEquals(java.util.concurrent.TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
		assertEquals(java.util.concurrent.TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
		assertEquals(java.util.concurrent.TimeUnit.HOURS, spec.refreshTimeUnit);
	}
	
	@Test
	public void testDisableCaching() {
		CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
		assertEquals(0, spec.maximumSize.longValue());
		assertEquals(null, spec.maximumWeight);
	}
	
	@Test
	public void testToCacheBuilder() {
		CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100");
		CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
		assertEquals(100, builder.maximumSize());
	}
	
	@Test
	public void testToParsableString() {
		CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100,expireAfterWrite=1h");
		String parsableString = spec.toParsableString();
		assertEquals("maximumSize=100,expireAfterWrite=1h", parsableString);
	}
	
	@Test
	public void testEquals() {
		CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100,expireAfterWrite=1h");
		CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100,expireAfterWrite=1h");
		assertEquals(true, spec1.equals(spec2));
	}
}
