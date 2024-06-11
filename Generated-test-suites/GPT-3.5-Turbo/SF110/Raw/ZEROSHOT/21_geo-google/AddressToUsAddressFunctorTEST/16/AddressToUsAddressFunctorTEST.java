import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Test case 1: empty GeoAddress
        GeoAddress addr = new GeoAddress();
        GeoUsAddress expected = new GeoUsAddress();
        GeoUsAddress actual = functor.execute(addr);
        assertEquals(expected, actual);
        
        // Test case 2: GeoAddress with all fields
        GeoAddress addr2 = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("United States");
        country.getCountryNameCode().add(countryNameCode);
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("State");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        country.setAdministrativeArea(adminArea);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("12345");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        ThoroughfareNameType thoroughfareNameType1 = new ThoroughfareNameType();
        thoroughfareNameType1.setContent("Street 1");
        ThoroughfareNameType thoroughfareNameType2 = new ThoroughfareNameType();
        thoroughfareNameType2.setContent("Street 2");
        locality.getThoroughfare().getThoroughfareName().add(thoroughfareNameType1);
        locality.getThoroughfare().getThoroughfareName().add(thoroughfareNameType2);
        adminArea.setLocality(locality);
        addr2.setAddressDetails(new AddressDetails());
        addr2.getAddressDetails().setCountry(country);
        GeoUsAddress expected2 = new GeoUsAddress();
        expected2.setCountry("United States");
        expected2.setState("State");
        expected2.setCity("City");
        expected2.setPostalCode("12345");
        expected2.setAddressLine1("Street 1");
        expected2.setAddressLine2("Street 2");
        GeoUsAddress actual2 = functor.execute(addr2);
        assertEquals(expected2, actual2);
    }

}