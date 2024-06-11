package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert. assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

public class ListPopulationTest {

    /**
     * Tests the constructor with a positive population limit.
     */
    @Test
    public void testListPopulationConstructorPositive() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests the constructor with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegative() {
        new ListPopulation(-1);
    }

    /**
     * Tests the constructor with a population limit equal to the number of chromosomes provided.
     */
    @Test
    public void testListPopulationConstructorSameSize() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {});
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(chromosomes.size(), population.getPopulationSize());
    }

    /**
     * Tests the constructor with a population limit smaller than the number of chromosomes provided.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorTooLarge() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        new ListPopulation(chromosomes, 1);
    }

    /**
     * Tests the setChromosomes method with a null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Tests the setChromosomes method with a list of chromosomes bigger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        population.setChromosomes(chromosomes);
    }

    /**
     * Tests the addChromosomes method with a null collection of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNull() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Tests the addChromosomes method with a collection of chromosomes bigger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        population.addChromosomes(chromosomes);
        population.addChromosomes(Collections.singletonList(new Chromosome() {}));
    }

    /**
     * Tests the getChromosomes method.
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomes();
        assertEquals(chromosomes, result);
    }

    /**
     * Tests the getChromosomeList method.
     */
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(chromosomes, result);
    }

    /**
     * Tests the addChromosome method with a null chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeNull() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    /**
     * Tests the addChromosome method with a chromosome that would exceed the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeTooLarge() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    /**
     * Tests the getFittestChromosome method with an empty population.
     */
    @Test
    public void testGetFittestChromosomeEmpty() {
        ListPopulation population = new ListPopulation(5);
        assertNull(population.getFittestChromosome());
    }

    /**
     * Tests the getFittestChromosome method with a population containing only one chromosome.
     */
    @Test
    public void testGetFittestChromosomeOne() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {}), 1);
        assertEquals(new Chromosome(), population.getFittestChromosome());
    }

    /**
     * Tests the getFittestChromosome method with a population containing multiple chromosomes.
     */
    @Test
    public void testGetFittestChromosomeMultiple() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        }, new Chromosome() {}, new Chromosome() {}), 3);
        assertEquals(new Chromosome() {}, population.getFittestChromosome());
    }

    /**
     * Tests the getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests the setPopulationLimit method with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    /**
     * Tests the setPopulationLimit method with a population limit smaller than the current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {}, new Chromosome() {}), 2);
        population.setPopulationLimit(1);
    }

    /**
     * Tests the setPopulationLimit method with a population limit equal to the current population size.
     */
    @Test
    public void testSetPopulationLimitEqual() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {}, new Chromosome() {}), 2);
        population.setPopulationLimit(2);
    }

    /**
     * Tests the setPopulationLimit method with a population limit greater than the current population size.
     */
    @Test
    public void testSetPopulationLimitGreater() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {}, new Chromosome() {}), 2);
        population.setPopulationLimit(3);
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests the getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(Arrays.asList(new Chromosome() {}, new Chromosome() {}), 2);
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation(chromosomes, 5);
        String expected = "[Chromosome, Chromosome]";
        String result = population.toString();
        assertEquals(expected, result);
    }

    /**
     * Tests the iterator method.
     */
    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        assertEquals(chromosomes, result);
    }
}