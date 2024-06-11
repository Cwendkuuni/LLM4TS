package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class JoinerTest {
	
	@Test
	public void testJoin() {
		assertEquals("1,2,3,4", Joiner.on(",").join(Arrays.asList(1, 2, 3, 4)));
		assertEquals("a-b-c", Joiner.on("-").join(Arrays.asList("a", "b", "c")));
		assertEquals("", Joiner.on(",").join(Arrays.asList()));
		assertEquals("1-2-3-4", Joiner.on("-").join(new Object[] {1, 2, 3, 4}));
		assertEquals("", Joiner.on("-").join(new Object[] {}));
		assertNull(Joiner.on(",").join((Iterable<?>) null));
		assertNull(Joiner.on(",").join((Iterator<?>) null));
		assertNull(Joiner.on(",").join((Object[]) null));
		assertNull(Joiner.on(",").join((Object) null));
		assertEquals("null-null-null", Joiner.on("-").join(null, null, null));
	}

	@Test
	public void testUseForNull() {
		assertEquals("1,null,3", Joiner.on(",").useForNull("null").join(Arrays.asList(1, null, 3)));
        assertEquals("1,null,3", Joiner.on(",").useForNull("null").join(Arrays.asList(1, null, 3).iterator()));
        assertEquals("1,null,3,4,null", Joiner.on(",").useForNull("null").join(new Object[] {1, null, 3, 4, null}));
	}

	@Test
	public void testSkipNulls() {
		assertEquals("1,3", Joiner.on(",").skipNulls().join(Arrays.asList(1, null, 3)));
		assertEquals("1,3", Joiner.on(",").skipNulls().join(Arrays.asList(1, null, 3).iterator()));
		assertEquals("1,3,4", Joiner.on(",").skipNulls().join(new Object[] {1, null, 3, 4, null}));
	}

	@Test
	public void testMapJoiner() throws IOException {
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);

		assertEquals("a=1,b=2,c=3", Joiner.on(",").withKeyValueSeparator("=").join(map.entrySet()));
		assertEquals("a=1,b=2,c=3", Joiner.on(",").withKeyValueSeparator("=").join(map));
        assertEquals("a=1,b=2,c=3", Joiner.on(",").withKeyValueSeparator("=").join(map.entrySet().iterator()));
        assertEquals("a=1,b=2,c=3", Joiner.on(",").withKeyValueSeparator("=").join(map.entrySet().iterator()));

        StringBuilder builder = new StringBuilder();
        Joiner.on(",").withKeyValueSeparator("=").appendTo(builder, map);

        assertEquals("a=1,b=2,c=3", builder.toString());
	}

	@Beta
	public void testAppendToAppendable() throws IOException {
		StringBuilder builder = new StringBuilder();
        Appendable appendable = Joiner.on(",").skipNulls().appendTo(builder, Arrays.asList(1, null, 3));
        Appendable appendable2 = Joiner.on(",").skipNulls().appendTo(builder, Arrays.asList("a", null, "b"));
        Appendable appendable3 = Joiner.on(",").skipNulls().appendTo(builder, new Object[] {1, null, 3});

        assertEquals("1,3a,b", appendable3.toString());
	}
	
	@Beta
	public void testAppendToAppendableWithIterator() throws IOException {
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = Arrays.asList("a", null, "b").iterator();
        Appendable appendable = Joiner.on(",").skipNulls().appendTo(builder, iterator);
        
        assertEquals("a,b", appendable.toString());
        assertEquals("", builder.toString());
	}
}