import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationConstructorWithChromosomesAndPopulationLimit() {
        Chromosome chromosome = new MockChromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithPopulationLimitLessThanSizeOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new MockChromosome();
        chromosomes.add(chromosome);
        chromosomes.add(chromosome);
        new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(Collections.singletonList(new MockChromosome()));
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome());
        chromosomes.add(new MockChromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new MockChromosome()));
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome());
        chromosomes.add(new MockChromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new MockChromosome()));
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new MockChromosome()));
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(1, chromosomes.size());
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        Chromosome fittestChromosome = population.getFittestChromosome();
        population.remove(fittestChromosome);
        population.addChromosome(new MockChromosome());
        Chromosome newFittestChromosome = population.getFittestChromosome();
        assertEquals(newFittestChromosome, fittestChromosome);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitWithPopulationSizeBiggerThanNewPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(1);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        assertEquals("[org.junit.Assert$MockChromosome@4d40820, org.junit.Assert$MockChromosome@16d8242]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new MockChromosome());
        population.addChromosome(new MockChromosome());
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> chromosomes = new ArrayList<>();
        while (iterator.hasNext()) {
            chromosomes.add(iterator.next());
        }
        assertEquals(2, chromosomes.size());
    }
}

class MockChromosome implements Chromosome {
    @Override
    public double getFitness() {
        return 0.0;
    }

    @Override
    public int compareTo(Chromosome o) {
        return 0;
    }
}