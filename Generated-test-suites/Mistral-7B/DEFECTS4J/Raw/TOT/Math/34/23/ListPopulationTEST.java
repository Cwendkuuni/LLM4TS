package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation pop = new ListPopulation(10);
        assertEquals(10, pop.getPopulationSize());
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithNullList() {
        try {
            new ListPopulation(null, 10);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyList() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(0, pop.getPopulationSize());
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithListBiggerThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        try {
            new ListPopulation(chromosomes, 1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithListSmallerThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        ListPopulation pop = new ListPopulation(chromosomes, 2);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(2, pop.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithNegativePopulationLimit() {
        try {
            new ListPopulation(Collections.<Chromosome> emptyList(), -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        List<Chromosome> chromosomesToAdd = new ArrayList<Chromosome>();
        chromosomesToAdd.add(new Chromosome() {});
        pop.addChromosomes(chromosomesToAdd);
        assertEquals(1, pop.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanLimit() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 1);
        List<Chromosome> chromosomesToAdd = new ArrayList<Chromosome>();
        chromosomesToAdd.add(new Chromosome() {});
        chromosomesToAdd.add(new Chromosome() {});
        pop.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        pop.setChromosomes(chromosomes);
        assertEquals(1, pop.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 1);
        pop.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        ListPopulation pop = new ListPopulation(chromosomes, 10);
        List<Chromosome> result = pop.getChromosomes();
        assertEquals(chromosomes, result);
    }
}