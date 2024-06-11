package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.Vector;
import java.util.stream.StreamTokenizer;

import org.junit.Before;
import org.junit.Test;

public class JSPredicateFormTest {
	
	private JSPredicateForm jspf;
	
	@Before
	public void setUp() {
		jspf = new JSPredicateForm();
	}
	
	@Test
	public void testJSPredicateFormInit() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
		jspf.JSPredicateFormInit(tokenizer);
		assertEquals(1, jspf.size());
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_NullTokenizer() {
		jspf.JSPredicateFormInit(null);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_ExpectedLeftParen() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate)"));
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_ReadingPredicateError() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_UnexpectedRightParen() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
		tokenizer.nextToken();
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_OperatorNameExpected() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(!)"));
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_UnexpectedTokenType() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call)"));
		tokenizer.nextToken();
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInit_UnexpectedTokenType_2() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
		tokenizer.nextToken();
		jspf.JSPredicateFormInit(tokenizer);
	}
	
	@Test
	public void testJSPredicateFormInit_Call() {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call)"));
		tokenizer.nextToken();
		jspf.JSPredicateFormInit(tokenizer);
		assertEquals(2, jspf.size());
	}

	@Test(expected = JSParserError.class)
	public void testPrint_EmptyList() {
		jspf.print();
	}
	
	@Test
	public void testPrint_NotPredicate() {
		jspf.addElement("not");
		jspf.addElement(new JSPredicateForm());
		jspf.print();
	}
	
	@Test
	public void testPrint_NormalPredicate() {
		jspf.addElement("predicate");
		JSTerm term = new JSTerm();
		term.setTerm("term");
		jspf.addElement(term);
		jspf.print();
	}
	
	@Test
	public void testToStr_EmptyList() {
		StringBuffer res = jspf.toStr();
		assertEquals("", res.toString());
	}
	
	@Test
	public void testToStr_NotPredicate() {
		jspf.addElement("not");
		jspf.addElement(new JSPredicateForm());
		StringBuffer res = jspf.toStr();
		assertEquals("(not )", res.toString());
	}
	
	@Test
	public void testToStr_NormalPredicate() {
		jspf.addElement("predicate");
		JSTerm term = new JSTerm();
		term.setTerm("term");
		jspf.addElement(term);
		StringBuffer res = jspf.toStr();
		assertEquals("(predicate term)", res.toString());
	}
	
	@Test
	public void testClonePF_NotPredicate() {
		JSPredicateForm clone = jspf.clonePF();
		assertEquals(0, clone.size());
	}
	
	@Test
	public void testClonePF_NormalPredicate() {
		jspf.addElement("predicate");
		JSTerm term = new JSTerm();
		term.setTerm("term");
		jspf.addElement(term);
		JSPredicateForm clone = jspf.clonePF();
		assertEquals(jspf.size(), clone.size());
	}
	
	@Test
	public void testApplySubstitutionPF_NotPredicate() {
		JSSubstitution alpha = new JSSubstitution();
		JSPredicateForm applied = jspf.applySubstitutionPF(alpha);
		assertEquals(0, applied.size());
	}
	
	@Test
	public void testApplySubstitutionPF_NormalPredicate() {
		jspf.addElement("predicate");
		JSTerm term = new JSTerm();
		term.setTerm("term");
		jspf.addElement(term);
		JSSubstitution alpha = new JSSubstitution();
		JSPredicateForm applied = jspf.applySubstitutionPF(alpha);
		assertEquals(jspf.size(), applied.size());
	}
	
	@Test
	public void testMatches_NotPredicate() {
		JSPredicateForm t = new JSPredicateForm();
		JSSubstitution beta = jspf.matches(t);
		assertTrue(beta.fail());
	}
	
	@Test
	public void testMatches_NormalPredicate_NotEqualFunctor() {
		jspf.addElement("predicate1");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate2");
		JSSubstitution beta = jspf.matches(t);
		assertTrue(beta.fail());
	}
	
	@Test
	public void testMatches_NormalPredicate_Matched() {
		jspf.addElement("predicate");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate");
		JSSubstitution beta = jspf.matches(t);
		assertFalse(beta.fail());
	}
	
	@Test
	public void testMatches_NotPredicate_WithAlpha() {
		JSPredicateForm t = new JSPredicateForm();
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution beta = jspf.matches(t, alpha);
		assertTrue(beta.fail());
	}
	
	@Test
	public void testMatches_NormalPredicate_NotEqualFunctor_WithAlpha() {
		jspf.addElement("predicate1");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate2");
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution beta = jspf.matches(t, alpha);
		assertTrue(beta.fail());
	}
	
	@Test
	public void testMatches_NormalPredicate_Matched_WithAlpha() {
		jspf.addElement("predicate");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate");
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution beta = jspf.matches(t, alpha);
		assertFalse(beta.fail());
	}
	
	@Test
	public void testEquals_NullPredicate() {
		JSPredicateForm t = null;
		boolean res = jspf.equals(t);
		assertFalse(res);
	}
	
	@Test
	public void testEquals_UnmatchedPredicateSize() {
		jspf.addElement("predicate1");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate1");
		boolean res = jspf.equals(t);
		assertFalse(res);
	}
	
	@Test
	public void testEquals_MatchedPredicate() {
		jspf.addElement("predicate");
		JSPredicateForm t = new JSPredicateForm();
		t.addElement("predicate");
		boolean res = jspf.equals(t);
		assertTrue(res);
	}
	
	@Test
	public void testStandarizerPredicateForm_NotPredicate() {
		JSPredicateForm nTA = jspf.standarizerPredicateForm();
		assertEquals(jspf.size(), nTA.size());
	}
	
	@Test
	public void testStandarizerPredicateForm_NormalPredicate() {
		jspf.addElement("predicate");
		JSTerm term = new JSTerm();
		term.setTerm("term");
		jspf.addElement(term);
		JSPredicateForm nTA = jspf.standarizerPredicateForm();
		assertEquals(jspf.size(), nTA.size());
	}

}