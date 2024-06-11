import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@Category(org.apache.commons.math3.genetics.PopulationTest.class)
public class ListPopulationTest {

    @Test
    public void testListPopulationConstructionWithPositiveLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructionWithNegativeLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationConstructionWithEmptyList() {
        List<Chromosome> chromosomes = Collections.emptyList();
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testListPopulationConstructionWithNonEmptyList() {
        Chromosome chromosome = new DummyChromosome(1);
        List<Chromosome> chromosomes = new ArrayList<>(2);
        chromosomes.add(chromosome);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructionWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>(6);
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>(2);
        Chromosome chromosome = new DummyChromosome(1);
        chromosomes.add(chromosome);
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(2), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(2), 5);
        List<Chromosome> chromosomes = new ArrayList<>(7);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)));
        Chromosome bestChromosome = population.getFittestChromosome();
        assertEquals(new DummyChromosome(1), bestChromosome);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new DummyChromosome(1));
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)));
        assertEquals("[DummyChromosome@" + population.hashCode() + "]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)));
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        Chromosome chromosome = iterator.next();
        assertEquals(new DummyChromosome(1), chromosome);
        assertFalse(iterator.hasNext());
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome{" + id + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DummyChromosome)) return false;
            DummyChromosome that = (DummyChromosome) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public int compareTo(Chromosome o) {
            DummyChromosome that = (DummyChromosome) o;
            return Integer.compare(id, that.id);
        }
    }
}