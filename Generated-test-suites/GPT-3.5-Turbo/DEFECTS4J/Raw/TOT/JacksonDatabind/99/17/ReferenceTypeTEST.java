
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

	@Test
	public void testConstructor() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		Assert.assertNotNull(referenceType);

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null,
				null, false);
		Assert.assertNotNull(referenceType);

		// Error Case
		try {
			referenceType = new ReferenceType(null, TypeBindings.emptyBindings(), null, null, null, null, null, null,
					false);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testUpgradeFrom() {
		// Typical Case
		JavaType simpleType = new SimpleType(String.class);
		JavaType referenceType = ReferenceType.upgradeFrom(simpleType, simpleType);
		Assert.assertNotNull(referenceType);

		// Edge Case
		referenceType = ReferenceType.upgradeFrom(simpleType, null);
		Assert.assertNotNull(referenceType);

		// Error Case
		try {
			referenceType = ReferenceType.upgradeFrom(null, simpleType);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testWithContentType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType modifiedType = referenceType.withContentType(stringType);
		Assert.assertEquals(referenceType, modifiedType);

		// Edge Case
		modifiedType = referenceType.withContentType(null);
		Assert.assertEquals(referenceType, modifiedType);
	}

	@Test
	public void testWithTypeHandler() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType modifiedType = referenceType.withTypeHandler(null);
		Assert.assertEquals(referenceType, modifiedType);

		// Edge Case
		modifiedType = referenceType.withTypeHandler(stringType);
		Assert.assertEquals(referenceType, modifiedType);
	}

	@Test
	public void testWithContentTypeHandler() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType modifiedType = referenceType.withContentTypeHandler(stringType);
		Assert.assertEquals(referenceType, modifiedType);

		// Edge Case
		modifiedType = referenceType.withContentTypeHandler(null);
		Assert.assertEquals(referenceType, modifiedType);
	}

	@Test
	public void testWithStaticTyping() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType modifiedType = referenceType.withStaticTyping();
		Assert.assertEquals(referenceType, modifiedType);

		// Edge Case
		modifiedType = referenceType.withStaticTyping();
		Assert.assertEquals(referenceType, modifiedType);
	}

	@Test
	public void testRefine() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType modifiedType = referenceType.refine(String.class, TypeBindings.emptyBindings(), null, null);
		Assert.assertEquals(referenceType, modifiedType);

		// Edge Case
		modifiedType = referenceType.refine(null, null, null, null);
		Assert.assertEquals(referenceType, modifiedType);
	}

	@Test
	public void testGetContentType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType contentType = referenceType.getContentType();
		Assert.assertEquals(stringType, contentType);

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null,
				null, false);
		contentType = referenceType.getContentType();
		Assert.assertNull(contentType);
	}

	@Test
	public void testGetReferencedType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType referencedType = referenceType.getReferencedType();
		Assert.assertEquals(stringType, referencedType);

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, stringType, null,
				null, null, false);
		referencedType = referenceType.getReferencedType();
		Assert.assertEquals(stringType, referencedType);
	}

	@Test
	public void testHasContentType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		Assert.assertTrue(referenceType.hasContentType());

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null,
				null, false);
		Assert.assertTrue(referenceType.hasContentType());
	}

	@Test
	public void testIsReferenceType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		Assert.assertTrue(referenceType.isReferenceType());

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null,
				null, false);
		Assert.assertTrue(referenceType.isReferenceType());
	}

	@Test
	public void testGetAnchorType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType anchorType = referenceType.getAnchorType();
		Assert.assertEquals(referenceType, anchorType);

		// Edge Case
		referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null,
				null, false);
		anchorType = referenceType.getAnchorType();
		Assert.assertEquals(referenceType, anchorType);
	}

	@Test
	public void testIsAnchorType() {
		// Typical Case
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		Assert.assertTrue(referenceType.isAnchorType());

		// Edge Case
		JavaType anchorType = referenceType.getAnchorType();
		JavaType notAnchorType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, stringType,
				null, null, null, false);
		Assert.assertFalse(notAnchorType.isAnchorType());
	}

	@Test
	public void testToString() {
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		String str = referenceType.toString();
		Assert.assertNotNull(str);
	}

	@Test
	public void testEquals() {
		JavaType stringType = new SimpleType(String.class);
		JavaType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);
		JavaType referenceType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null,
				stringType, null, null, false);

		Assert.assertTrue(referenceType1.equals(referenceType2));
	}
}

