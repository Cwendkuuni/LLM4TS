package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

	@Test
	public void testAndMatches() {
		Element root = new Element("root");
		Element node = new Element("node");

		Evaluator evaluator1 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return true;
			}
		};

		Evaluator evaluator2 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return true;
			}
		};

		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(evaluator1);
		evaluators.add(evaluator2);

		CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

		assertTrue(andEvaluator.matches(root, node));
	}

	@Test
	public void testAndDoesNotMatch() {
		Element root = new Element("root");
		Element node = new Element("node");

		Evaluator evaluator1 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return false;
			}
		};

		Evaluator evaluator2 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return true;
			}
		};

		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(evaluator1);
		evaluators.add(evaluator2);

		CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

		assertFalse(andEvaluator.matches(root, node));
	}

	@Test
	public void testOrMatches() {
		Element root = new Element("root");
		Element node = new Element("node");

		Evaluator evaluator1 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return false;
			}
		};

		Evaluator evaluator2 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return true;
			}
		};

		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(evaluator1);
		evaluators.add(evaluator2);

		CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

		assertTrue(orEvaluator.matches(root, node));
	}

	@Test
	public void testOrDoesNotMatch() {
		Element root = new Element("root");
		Element node = new Element("node");

		Evaluator evaluator1 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return false;
			}
		};

		Evaluator evaluator2 = new Evaluator() {
			@Override
			public boolean matches(Element root, Element node) {
				return false;
			}
		};

		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(evaluator1);
		evaluators.add(evaluator2);

		CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

		assertFalse(orEvaluator.matches(root, node));
	}

}