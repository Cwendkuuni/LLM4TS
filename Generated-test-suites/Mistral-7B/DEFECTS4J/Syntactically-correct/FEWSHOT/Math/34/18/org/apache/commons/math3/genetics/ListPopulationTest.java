package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyListAndLimit() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertTrue(pop.isEmpty());
    }

    @Test
    public void testListPopulationWithNullListAndLimit() {
        try {
            new ListPopulation(null, 5);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyListAndLimitBiggerThanZero() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertTrue(pop.isEmpty());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativeLimit() {
        new ListPopulation(Collections.emptyList(), -1);
    }

    @Test
    public void testListPopulationWithNullListAndLimitEqualSize() {
        List<Chromosome> list = new ArrayList<>();
        ListPopulation pop = new ListPopulation(list, list.size());
        assertEquals(list.size(), pop.getPopulationLimit());
        assertEquals(list.size(), pop.getPopulationSize());
        assertSame(list.get(0), pop.getChromosomes().get(0));
        assertFalse(pop.isEmpty());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListBiggerThanLimit() {
        List<Chromosome> list = new ArrayList<>();
        list.add(new Object()[0]);
        list.add(new Object()[0]);
        ListPopulation pop = new ListPopulation(list, list.size() + 1);
    }

    @Test
    public void testListPopulationWithAddChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosomes(Collections.singletonList(new Object()[0]));
        assertEquals(1, pop.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddChromosomesBiggerThanLimit() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosomes(Collections.singletonList(new Object()[0]));
        pop.addChromosomes(Collections.singletonList(new Object()[0]));
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        assertEquals("[Ljava.lang.Object;@" + pop.hashCode() + "]", pop.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        Iterator<Chromosome> it = pop.iterator();
        assertTrue(it.hasNext());
        assertEquals(it.next(), pop.getChromosomes().get(0));
        assertFalse(it.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationIteratorRemove() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        Iterator<Chromosome> it = pop.iterator();
        it.remove();
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegative() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        pop.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        pop.setPopulationLimit(0);
    }

    @Test
    public void testListPopulationSetPopulationLimitBiggerThanCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        pop.setPopulationLimit(2);
        assertTrue(pop.getChromosomes().add(new Object()[0]));
        assertTrue(pop.getChromosomes().add(new Object()[0]));
        assertEquals(2, pop.getPopulationSize());
    }

    @Test
    public void testListPopulationSetChromosomesNull() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        try {
            pop.setChromosomes(null);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanLimit() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        List<Chromosome> biggerList = new ArrayList<>();
        biggerList.add(new Object()[0]);
        biggerList.add(new Object()[0]);
        pop.setChromosomes(biggerList);
    }

    @Test
    public void testListPopulationSetChromosomesEquivalentToCurrentList() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        List<Chromosome> equivalentList = new ArrayList<>(pop.getChromosomes());
        pop.setChromosomes(equivalentList);
        assertEquals(pop.getChromosomes(), equivalentList);
    }

    @Test
    public void testListPopulationSetChromosomesSmallerThanCurrentList() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Object()[0]));
        List<Chromosome> smallerList = new ArrayList<>();
        pop.setChromosomes(smallerList);
        assertEquals(0, pop.getPopulationSize());
        assertTrue(pop.isEmpty());
    }

}