package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class ListPopulationTest {

    private static final int POPULATION_LIMIT = 5;

    private ListPopulation population;
    private List<Chromosome> initialChromosomes;

    public ListPopulationTest(List<Chromosome> initialChromosomes) {
        this.initialChromosomes = initialChromosomes;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { Arrays.asList(new Chromosome(1.0f), new Chromosome(2.0f)) },
                { Arrays.asList(new Chromosome(1.0f), new Chromosome(3.0f), new Chromosome(4.0f)) },
                { Arrays.asList(new Chromosome(1.0f), null) }
        });
    }

    @Before
    public void setUp() {
        this.population = new ListPopulation(POPULATION_LIMIT);
        this.population.addChromosomes(this.initialChromosomes);
    }

    @Test
    public void testListPopulationInitialization() {
        assertEquals(this.initialChromosomes.size(), this.population.getPopulationSize());
        assertEquals(this.initialChromosomes, this.population.getChromosomes());
        assertEquals(POPULATION_LIMIT, this.population.getPopulationLimit());
        assertNotNull(this.population.toString());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        ListPopulation population = new ListPopulation(POPULATION_LIMIT, null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationInitializationWithTooManyChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome(1.0f), new Chromosome(2.0f),
                new Chromosome(3.0f), new Chromosome(4.0f), new Chromosome(5.0f));
        ListPopulation population = new ListPopulation(POPULATION_LIMIT, initialChromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome(5.0f), new Chromosome(6.0f));
        this.population.setChromosomes(newChromosomes);
        assertEquals(newChromosomes.size(), this.population.getPopulationSize());
        assertEquals(newChromosomes, this.population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        this.population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithTooManyChromosomes() {
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome(1.0f), new Chromosome(2.0f),
                new Chromosome(3.0f), new Chromosome(4.0f), new Chromosome(5.0f),
                new Chromosome(6.0f));
        this.population.setChromosomes(newChromosomes);
    }

    @Test
    public void testListPopulationAddChromosome() {
        Chromosome newChromosome = new Chromosome(7.0f);
        this.population.addChromosome(newChromosome);
        assertEquals(this.initialChromosomes.size() + 1, this.population.getPopulationSize());
        assertEquals(newChromosome, this.population.getChromosomes().get(this.initialChromosomes.size()));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddNullChromosome() {
        this.population.addChromosome(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitExceeded() {
        this.population.addChromosome(new Chromosome(7.0f));
        this.population.addChromosome(new Chromosome(8.0f));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome fittestChromosome = this.population.getFittestChromosome();
        for (Chromosome chromosome : this.population.getChromosomes()) {
            if (chromosome.compareTo(fittestChromosome) > 0) {
                fail("Wrong fittest chromosome. Expected: " + fittestChromosome + ", Found: " + chromosome);
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationIteratorRemove() {
        Iterator<Chromosome> iterator = this.population.iterator();
        iterator.next();
        iterator.remove();
    }
}