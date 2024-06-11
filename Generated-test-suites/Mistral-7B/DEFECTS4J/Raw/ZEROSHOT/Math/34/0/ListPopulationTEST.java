import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation listPopulation = new ListPopulation(5);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(-5);
    }

    @Test
    public void testListPopulationWithValidListOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 5);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(2, listPopulation.getPopulationSize());
        assertEquals(chromosome1, listPopulation.getChromosomes().get(0));
        assertEquals(chromosome2, listPopulation.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullListOfChromosomes() {
        ListPopulation listPopulation = new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListOfChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testAddChromosomeToPopulation() {
        ListPopulation listPopulation = new ListPopulation(5);
        Chromosome chromosomeToAdd = new DummyChromosome(6);
        listPopulation.addChromosome(chromosomeToAdd);
        assertEquals(6, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeToPopulationWithPopulationLimitReached() {
        ListPopulation listPopulation = new ListPopulation(5);
        Chromosome chromosomeToAdd = new DummyChromosome(6);
        listPopulation.addChromosome(chromosomeToAdd);
        listPopulation.addChromosome(new DummyChromosome(7));
    }

    @Test
    public void testAddCollectionOfChromosomesToPopulation() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(6);
        Chromosome chromosome2 = new DummyChromosome(7);
        chromosomesToAdd.add(chromosome1);
        chromosomesToAdd.add(chromosome2);

        listPopulation.addChromosomes(chromosomesToAdd);
        assertEquals(7, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddCollectionOfChromosomesToPopulationWithPopulationLimitReached() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(6);
        Chromosome chromosome2 = new DummyChromosome(7);
        Chromosome chromosome3 = new DummyChromosome(8);
        chromosomesToAdd.add(chromosome1);
        chromosomesToAdd.add(chromosome2);
        chromosomesToAdd.add(chromosome3);

        listPopulation.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(99);
        Chromosome chromosome3 = new DummyChromosome(98);
        Chromosome chromosome4 = new DummyChromosome(101);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        listPopulation.addChromosome(chromosome4);

        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        assertEquals(chromosome1.getFitness(), fittestChromosome.getFitness());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(-5);
    }

    @Test
    public void testSetPopulationLimitWithCurrentPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithCurrentPopulationSizeMinusOne() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(4);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(99);
        Chromosome chromosome3 = new DummyChromosome(98);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        listPopulation.setChromosomes(chromosomes);
        assertEquals(3, listPopulation.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation listPopulation = new ListPopulation(5);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(99);
        Chromosome chromosome3 = new DummyChromosome(98);
        Chromosome chromosome4 = new DummyChromosome(97);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);

        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(99);
        Chromosome chromosome3 = new DummyChromosome(98);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        listPopulation.setChromosomes(chromosomes);
        assertEquals("[DummyChromosome[fitness=100.0], DummyChromosome[fitness=99.0], DummyChromosome[fitness=98.0]]", listPopulation.toString());
    }
}

class DummyChromosome implements Chromosome {
    private double fitness;

    public DummyChromosome(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Chromosome another) {
        return Double.compare(this.getFitness(), another.getFitness());
    }
}